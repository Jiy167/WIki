<template>
  <a-layout-header class="header">
    <div class="logo" />

    <a-menu
        v-model:selectedKeys="selectedKeys1"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px', width: '800px' }"
        class="menu"
    >
      <a-menu-item key="/home">
        <router-link to="/">Home</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/user" :style="user.id? {} : {display:'none'}">
        <router-link to="/admin/user">User manager</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/ebook" :style="user.id? {} : {display:'none'}">
        <router-link to="/admin/ebook">Ebook manager</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/category" :style="user.id? {} : {display:'none'}">
        <router-link to="/admin/category">Category manager</router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">About</router-link>
      </a-menu-item>

    </a-menu>
    <a-popconfirm
        title="Confirm to log out??"
        ok-text="Yes"
        cancel-text="No"
        @confirm="logout()"
    >
      <a class="login-menu" v-show="user.id">
        <span>log out</span>
      </a>
    </a-popconfirm>
    <a class="login-menu" v-show="user.id">
      <span>Hello:{{user.name}}</span>
    </a>
    <a class="login-menu" v-show="!user.id" @click="showLoginModal">
      <span>Login</span>
    </a>
    <a-modal
          title="Login"
          v-model:visible="loginModalVisible"
          :confirm-loading="loginModalLoading"
          @ok="login"
      >
        <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
          <a-form-item label="login name">
            <a-input v-model:value="loginUser.loginName" />
          </a-form-item>
          <a-form-item label="password">
            <a-input v-model:value="loginUser.password" type="password" />
          </a-form-item>
        </a-form>
      </a-modal>

  </a-layout-header>
</template>

<script lang="ts">
import {computed, defineComponent, ref} from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import store from "@/store";

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header',
  setup () {
    const user = computed(() => store.state.user);

    const loginUser = ref({
      loginName: "test",
      password: "test123"
    });
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true;
    };

    // login
    const login = () => {
      console.log("start login");
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post('/user/login', loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("login successful");
          // user.value = data.content;
          store.commit("setUser", data.content);
        } else {
          message.error(data.message);
        }
      });
    };

    const logout = () => {
      console.log("start to log out");
      axios.get('/user/logout/' + user.value.token).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("log out successful!");
          store.commit("setUser", {});
        } else {
          message.error(data.message);
        }
      });
    };

    return {
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      loginUser,
      login,
      user,
      logout,

    }
  }
});
</script>

<style>
.menu {
  float: left;
}

.login-menu {
  float: right;
  color: white;
  margin-right: 20px;
}
</style>