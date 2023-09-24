import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import axios from 'axios';
import {Tool} from "@/util/tool";
import { message } from 'ant-design-vue';

axios.defaults.baseURL = process.env.VUE_APP_SERVER;

axios.interceptors.request.use(function (config) {
    console.log('request params：', config);
    const token = store.state.user.token;
    if (Tool.isNotEmpty(token)) {
        config.headers.token = token;
        console.log("Request headers to add token:", token);
    }
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response) {
    console.log('response results：', response);
    return response;
}, error => {
    console.log('response error：', error);
    const response = error.response;
    const status = response.status;
    if (status === 401) {
        // Determine the status code is 401 and jump to the home page or login page
        console.log("Not logged in, jump to homepage");
        store.commit("setUser", {});
        message.error("Not logged in or logged in timeout");
        router.push('/');
    }
    return Promise.reject(error);
});


createApp(App).use(router).use(Antd).mount('#app')


console.log('environment：', process.env.NODE_ENV);
console.log('server：', process.env.VUE_APP_SERVER);