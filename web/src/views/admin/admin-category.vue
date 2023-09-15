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
          <a-input v-model:value="param.name" placeholder="name">
            <template #prefix><UserOutlined style="color: rgba(0, 0, 0, 0.25)" /></template>
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
               :data-source="categorys"
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

          <template v-if="column.key === 'cover'">
            <img :src="record.cover" alt="Cover" style="max-width: 30px; max-height: 30px;" />
          </template>

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
      title="category forms"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="formState" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-item label="name">
        <a-input v-model:value="category.name" />
      </a-form-item>
      <a-form-item label="father-category">
        <a-input v-model:value="category.parent" />
      </a-form-item>
      <a-form-item label="order">
        <a-input v-model:value="category.sort" />
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
  components: {
    SmileOutlined,
    DownOutlined,
  },
  setup() {
    const param = ref();
    param.value = {};
    const categorys = ref();
    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
        width: 350,
      },
      {
        title: 'father-Category1',
        dataIndex: 'parent',
        key: 'parent',
      },
      {
        title: 'order',
        key: 'sort',
        dataIndex: 'sort',
      },
      {
        title: 'Action',
        key: 'action',
      },
    ];

    // const category = [
    //   {
    //     key: '1',
    //     cover: '/image/cover11.png',
    //     name: 'new',
    //     category1Id: '1',
    //     category2Id: '2',
    //     docCount: '3',
    //     viewCount: 4,
    //     voteCount: 100,
    //     age: 32,
    //
    //   },
    //
    // ];

    /**
     * Data query
     **/
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/category/list", {
        params: {
          page: params.page,
          size: params.size,
          name: param.value.name,
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if(data.success){
          categorys.value = data.content.list;

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
    const category = ref({});
    // const modalText = ref<string>('Content of the modal');
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);



    const handleModalOk = () => {
      // modalText.value = 'The modal will be closed after two seconds';
      modalLoading.value = true;

      axios.post("/category/save", category.value).then((response) => {
        modalLoading.value = false;
        const data = response.data; //data = commonResp
        if(data.success){
          modalVisible.value = false;

          //load list again
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        }else{
          message.error(data.message);
        }
      });
    };

    //edit
    const edit = (record: any) => {
      modalVisible.value = true;
      category.value = Tool.copy(record);
    };

    //add
    const add = () => {
      modalVisible.value = true;
      category.value = {};
    };

    //delete
    const handleDelete = (id: number) => {
      axios.delete("/category/delete/" + id).then((response) => {
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
      categorys,
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
      category,


    };
  },
});
</script>

