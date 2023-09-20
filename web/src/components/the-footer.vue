<template>
  <a-layout-footer style="text-align: center">
    Jiyuan ebooks<span v-show="user.id">, hello: {{user.name}}</span>
  </a-layout-footer>
</template>

<script lang="ts">
import { defineComponent, computed, onMounted } from 'vue';
import store from "@/store";
import {Tool} from "@/util/tool";


export default defineComponent({
  name: 'the-footer',
  setup() {
    const user = computed(() => store.state.user);


    let websocket: any;
    let token: any;
    const onOpen = () => {
      console.log('WebSocket connection successful, status code: ', websocket.readyState)
    };
    const onMessage = (event: any) => {
      console.log('WebSocket received message: ', event.data);
    };
    const onError = () => {
      console.log('WebSocket connection error, status code: ', websocket.readyState)
    };
    const onClose = () => {
      console.log('WebSocket connection closed, status code: ', websocket.readyState)
    };
    const initWebSocket = () => {
      // connection succeeded
      websocket.onopen = onOpen;
      //Callback when message is received
      websocket.onmessage = onMessage;
      // connection error
      websocket.onerror = onError;
      //Callback for connection closing
      websocket.onclose = onClose;
    };

    onMounted(() => {
      //WebSocket
      if ('WebSocket' in window) {
        token = Tool.uuid(10);
        //Connection address: ws://127.0.0.1:8880/ws/xxx
        websocket = new WebSocket(process.env.VUE_APP_WS_SERVER + '/ws/' + token);
        initWebSocket()

        // closure
        // websocket.close();
      } else {
        alert('The current browser does not support')
      }
    });


    return {
      user
    }
  }
});
</script>