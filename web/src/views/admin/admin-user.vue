<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-form
          layout="inline"
          :model="param"
      >
        <a-form-item>
          <a-input v-model:value="param.loginName" placeholder="loginName">
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
            Query
          </a-button>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="add()">
            Add
          </a-button>
        </a-form-item>
      </a-form>

      <a-table :columns="columns"
               :data-source="users"
               :pagination="pagination"
               :loading="loading"
               @change="handleTableChange"
      >
        <template #headerCell="{ column }">
          <template v-if="column.key === 'name'">
        <span>
          <smile-outlined />
          Name
        </span>
          </template>
        </template>

        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'action'">
            <span>
              <a-space size="small">
                <a-button type="primary" @click="edit(record)">
                  edit
                </a-button>
                <a-popconfirm
                    title="Are you sure delete this task?"
                    ok-text="Yes"
                    cancel-text="No"
                    @confirm="handleDelete(record.id)"
                >
                  <a-button type="danger">
                    delete
                  </a-button>
                </a-popconfirm>

              </a-space>
            </span>
          </template>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
  <a-modal
      v-model:visible="modalVisible"
      title="user forms"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="formState" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-item label="loginName">
        <a-input v-model:value="user.loginName" :disabled="!!user.id"/>
      </a-form-item>
      <a-form-item label="nickName">
        <a-input v-model:value="user.name" />
      </a-form-item>
      <a-form-item label="password">
        <a-input v-model:value="user.password" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import { SmileOutlined, DownOutlined, } from '@ant-design/icons-vue';
import { defineComponent, ref, onMounted } from 'vue';
import axios from 'axios';
import { message } from 'ant-design-vue';
import {Tool} from "@/util/tool";

export default defineComponent({
  name: 'AdminUser',
  components: {
    SmileOutlined,
    DownOutlined,
  },
  setup() {
    const param = ref();
    param.value = {};
    const users = ref();
    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: 'loginName',
        key: 'loginName',
        dataIndex: 'loginName',
      },
      {
        title: 'nickName',
        dataIndex: 'name',
        key: 'name',
      },
      {
        title: 'password',
        dataIndex: 'password',
        key: 'password',
      },
      {
        title: 'Action',
        key: 'action',
      },
    ];


    /**
     * Data query
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      user.value = [];
      axios.get("/user/list", {
        params: {
          page: params.page,
          size: params.size,
          name: param.value.name,
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if(data.success){
          users.value = data.content.list;

          //reset pagination button
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        }
        else{
          message.error(data.message);
        }

      });
    };

    /**
     * Triggered when page number is clicked
     */
    const handleTableChange = (pagination: any) => {
      console.log("pagination params：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    // -------- forms ---------
    const categoryIds = ref();
    const user = ref();
    // const modalText = ref<string>('Content of the modal');
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);



    const handleModalOk = () => {
      modalLoading.value = true;
      axios.post("/user/save", user.value).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {
          modalVisible.value = false;

          // load list again
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    };

    //edit
    const edit = (record: any) => {
      modalVisible.value = true;
      user.value = Tool.copy(record);
      categoryIds.value = [user.value.category1Id, user.value.category2Id]
    };

    //add
    const add = () => {
      modalVisible.value = true;
      user.value = {};
    };

    //delete
    const handleDelete = (id: number) => {
      axios.delete("/user/delete/" + id).then((response) => {
        const data = response.data; //data = commonResp
        if(data.success){
          //load list again
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        }
      });
    };

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      });
    });

    return {
      param,
      users,
      columns,
      pagination,
      loading,
      handleTableChange,
      handleQuery,

      edit,
      add,
      handleDelete,


      // modalText,
      modalVisible,
      modalLoading,
      handleModalOk,
      user,




    };
  },
});
</script>

