import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import axios from 'axios';

axios.defaults.baseURL = process.env.VUE_APP_SERVER;

axios.interceptors.request.use(function (config) {
    console.log('request params：', config);
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response) {
    console.log('response results：', response);
    return response;
}, error => {
    console.log('response error：', error);
    return Promise.reject(error);
});


createApp(App).use(router).use(Antd).mount('#app')


console.log('environment：', process.env.NODE_ENV);
console.log('server：', process.env.VUE_APP_SERVER);