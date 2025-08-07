<template>
  <div class="app-container">
    <!-- Table Selection -->
    <el-card class="box-card" style="margin-bottom: 20px;">
      <div slot="header" class="clearfix">
        <span>加入餐桌</span>
      </div>
      <el-form :inline="true" @submit.native.prevent>
        <el-form-item label="桌号">
          <el-input v-model="tableIdInput" placeholder="请输入桌号, 如 T1"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="joinTable">加入/切换餐桌</el-button>
          <el-button type="danger" @click="leaveTable" :disabled="!isConnected">离开餐桌</el-button>
        </el-form-item>
      </el-form>
      <div v-if="isConnected">当前餐桌: <el-tag type="success">{{ tableId }}</el-tag></div>
    </el-card>

    <el-row :gutter="20">
      <!-- Product List -->
      <el-col :span="14">
        <el-card class="box-card" header="商品菜单">
          <el-row :gutter="15">
            <el-col :span="8" v-for="product in products" :key="product.id" style="margin-bottom: 15px;">
              <el-card shadow="hover">
                <div class="product-item">
                  <h4>{{ product.name }}</h4>
                  <p>价格: {{ product.price | currency }}</p>
                  <el-button type="success" plain @click="handleAddItem(product)" :disabled="!isConnected">加入购物车</el-button>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-card>
      </el-col>

      <!-- Shopping Cart -->
      <el-col :span="10">
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <span>实时购物车</span>
                <el-tag v-if="!isConnected" type="info" style="float: right;">未连接</el-tag>
                <el-tag v-if="isConnected" type="success" style="float: right;">已连接</el-tag>
            </div>
            <div v-if="!isConnected">
                <p>请输入桌号加入餐桌以开始点餐。</p>
            </div>
            <div v-else>
                <el-table :data="cart.items" style="width: 100%">
                    <el-table-column prop="itemName" label="商品"></el-table-column>
                    <el-table-column prop="price" label="单价" width="80">
                        <template slot-scope="scope">{{ scope.row.price | currency }}</template>
                    </el-table-column>
                    <el-table-column label="数量" width="150">
                        <template slot-scope="scope">
                            <el-input-number v-model="scope.row.quantity" @change="(quantity) => handleUpdateQuantity(scope.row.itemId, quantity)" :min="1" size="mini"></el-input-number>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="80">
                        <template slot-scope="scope">
                            <el-button type="danger" icon="el-icon-delete" circle size="mini" @click="handleRemoveItem(scope.row.itemId)"></el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div style="margin-top: 20px; text-align: right;">
                    <h3>总计: <el-tag type="danger" effect="dark">{{ cart.total | currency }}</el-tag></h3>
                </div>
            </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapState, mapActions } from 'vuex';

export default {
  name: "Cart",
  data() {
    return {
      tableIdInput: 'T1',
      products: [
        { id: 'P001', name: '招牌牛肉汉堡', price: 28.00 },
        { id: 'P002', name: '香辣鸡腿堡', price: 25.00 },
        { id: 'P003', name: '经典薯条', price: 12.50 },
        { id: 'P004', name: '可口可乐', price: 6.00 },
        { id: 'P005', name: '田园蔬菜沙拉', price: 18.00 },
        { id: 'P006', name: '巧克力奶昔', price: 15.00 },
      ]
    };
  },
  computed: {
    ...mapState('cart', [
      'isConnected',
      'tableId',
      'cart'
    ])
  },
  methods: {
    ...mapActions('cart', [
      'connect',
      'disconnect',
      'addItem',
      'removeItem',
      'updateQuantity'
    ]),
    joinTable() {
      if (this.tableIdInput) {
        this.connect(this.tableIdInput);
      }
    },
    leaveTable() {
      this.disconnect();
    },
    handleAddItem(product) {
        const addItemRequest = {
            itemId: product.id,
            itemName: product.name,
            price: product.price
        };
        this.addItem(addItemRequest);
    },
    handleRemoveItem(itemId) {
      this.removeItem(itemId);
    },
    handleUpdateQuantity(itemId, quantity) {
      this.updateQuantity({ itemId, quantity });
    }
  },
  filters: {
      currency(value) {
          if (typeof value !== "number") {
              value = Number(value) || 0;
          }
          return `¥${value.toFixed(2)}`;
      }
  },
  beforeDestroy() {
      // Clean up connection when component is destroyed
      this.disconnect();
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}
.box-card {
    min-height: 300px;
}
.product-item {
    text-align: center;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}
</style>