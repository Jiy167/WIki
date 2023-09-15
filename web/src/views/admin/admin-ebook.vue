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
               :data-source="ebooks"
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

          <template v-if="column.key === 'category'">
<!--            {{column}}======{{record}}-->
            <span>{{ getCategoryName(record.category1Id) }} / {{ getCategoryName(record.category2Id) }}</span>
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
      title="ebook forms"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <a-form :model="formState" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-form-item label="cover">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="name">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="category">
        <a-cascader
            v-model:value="categoryIds"
            :field-names="{ label: 'name', value: 'id', children: 'children' }"
            :options="level1"
        />
      </a-form-item>

      <a-form-item label="description">
        <a-input v-model:value="ebook.description" type="textarea"/>
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
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: 'Cover',
        key: 'cover',
        dataIndex: 'cover',
      },
      {
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
        width: 350,
      },
      {
        title: 'Category',
        dataIndex: 'category',
        key: 'category',
      },
      {
        title: 'Docs',
        key: 'docCount',
        dataIndex: 'docCount',
      },
      {
        title: 'Views',
        key: 'viewCount',
        dataIndex: 'viewCount',
      },
      {
        title: 'Likes',
        key: 'voteCount',
        dataIndex: 'voteCount',
      },
      {
        title: 'Action',
        key: 'action',
      },
    ];

    // const ebook = [
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
      ebook.value = [];
      axios.get("/ebook/list", {
        params: {
          page: params.page,
          size: params.size,
          name: param.value.name,
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if(data.success){
          ebooks.value = data.content.list;

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
    const ebook = ref();
    // const modalText = ref<string>('Content of the modal');
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);



    const handleModalOk = () => {
      // modalText.value = 'The modal will be closed after two seconds';
      modalLoading.value = true;
      ebook.value.category1Id = categoryIds.value[0];
      ebook.value.category2Id = categoryIds.value[1];

      axios.post("/ebook/save", ebook.value).then((response) => {
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
      ebook.value = Tool.copy(record);
      categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id]
    };

    //add
    const add = () => {
      modalVisible.value = true;
      ebook.value = {};
    };

    //delete
    const handleDelete = (id: number) => {
      axios.delete("/ebook/delete/" + id).then((response) => {
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

    const level1 =  ref();
    let categorys: any;
    /**
     * query all categories
     **/
    const handleQueryCategory = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          console.log("original array：", categorys);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("tree structure：", level1.value);

          //load ebook after loading category
          handleQuery({
            page: 1,
            size: pagination.value.pageSize,
          });

        } else {
          message.error(data.message);
        }
      });
    };

    const getCategoryName = (cid: number) => {
      // console.log(cid)
      let result = "";
      categorys.forEach((item: any) => {
        if (item.id === cid) {
          // return item.name;
          result = item.name;
        }
      });
      return result;
    };



    onMounted(() => {
      handleQueryCategory();
    });

    return {
      param,
      ebooks,
      columns,
      pagination,
      loading,
      handleTableChange,
      handleQuery,
      getCategoryName,

      edit,
      add,
      handleDelete,


      // modalText,
      modalVisible,
      modalLoading,
      handleModalOk,
      ebook,
      categoryIds,
      level1,


    };
  },
});
</script>

