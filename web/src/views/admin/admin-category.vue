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
          <a-button type="primary" @click="handleQuery()">
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
               :data-source="level1"
               :pagination="false"
               :loading="loading"
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
      <a-form-item label="parent-category">
        <a-space>
          <a-select
              ref="select"
              v-model:value="category.parent"
              style="width: 300px"
          >
            <a-select-option value="0">
              none
            </a-select-option>
            <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="category.id === c.id">
              {{c.name}}
            </a-select-option>
          </a-select>
        </a-space>
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
  name: 'AdminCategory',
  components: {
    SmileOutlined,
    DownOutlined,
  },
  setup() {
    const param = ref();
    param.value = {};
    const categorys = ref();
    const loading = ref(false);

    /**
     * The first-level classification tree, the children attribute is the second-level classification
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // The first-level classification tree, the children attribute is the second-level classificati

    const columns = [
      {
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
        width: 350,
      },
      {
        title: 'parent-category',
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
    const handleQuery = () => {
      loading.value = true;
      level1.value = [];
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if(data.success){
          categorys.value = data.content;
          console.log("original array：", categorys.value);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys.value, 0);
          console.log("tree structure：", level1);
        }
        else{
          message.error(data.message);
        }
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
          handleQuery();
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
          handleQuery();
        }
      });
    };

    onMounted(() => {
      handleQuery();
    });

    return {
      param,
      // categorys,
      level1,
      columns,
      loading,
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

