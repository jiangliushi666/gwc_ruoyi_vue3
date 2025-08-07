import Stomp from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { getToken } from '@/utils/auth';

const state = {
    stompClient: null,
    isConnected: false,
    tableId: null,
    cart: {
        items: [],
        total: 0
    }
};

const mutations = {
    SET_STOMP_CLIENT: (state, client) => {
        state.stompClient = client;
    },
    SET_CONNECTED: (state, isConnected) => {
        state.isConnected = isConnected;
    },
    SET_TABLE_ID: (state, tableId) => {
        state.tableId = tableId;
    },
    UPDATE_CART: (state, cartPayload) => {
        state.cart = cartPayload;
    }
};

const actions = {
    connect({ commit, dispatch, rootState }, tableId) {
        if (state.isConnected) {
            dispatch('disconnect');
        }

        const socket = new SockJS(`${process.env.VUE_APP_BASE_API}/ws`);
        const stompClient = Stomp.over(socket);
        const headers = { Authorization: 'Bearer ' + getToken() };

        stompClient.connect(headers, () => {
            commit('SET_STOMP_CLIENT', stompClient);
            commit('SET_CONNECTED', true);
            commit('SET_TABLE_ID', tableId);

            stompClient.subscribe(`/topic/cart/${tableId}`, (message) => {
                const cart = JSON.parse(message.body);
                commit('UPDATE_CART', cart);
            });

            // Get initial state
            dispatch('getCartState');

        }, (error) => {
            console.error('STOMP connection error:', error);
            commit('SET_CONNECTED', false);
        });
    },

    disconnect({ commit }) {
        if (state.stompClient && state.stompClient.connected) {
            state.stompClient.disconnect();
        }
        commit('SET_STOMP_CLIENT', null);
        commit('SET_CONNECTED', false);
        commit('SET_TABLE_ID', null);
        commit('UPDATE_CART', { items: [], total: 0 });
    },

    getCartState({ state }) {
        if (state.isConnected) {
            state.stompClient.send(`/app/cart/${state.tableId}/get`, {}, '');
        }
    },

    addItem({ state }, item) {
        if (state.isConnected) {
            state.stompClient.send(`/app/cart/${state.tableId}/addItem`, {}, JSON.stringify(item));
        }
    },

    removeItem({ state }, itemId) {
        if (state.isConnected) {
            state.stompClient.send(`/app/cart/${state.tableId}/removeItem`, {}, itemId);
        }
    },

    updateQuantity({ state }, { itemId, quantity }) {
        if (state.isConnected) {
            state.stompClient.send(`/app/cart/${state.tableId}/updateQuantity?quantity=${quantity}`, {}, itemId);
        }
    }
};

export default {
    namespaced: true,
    state,
    mutations,
    actions
};
