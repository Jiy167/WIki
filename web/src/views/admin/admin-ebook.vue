<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >

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

          <template v-if="column.key === 'action'">
        <span>
          <a-space size="small">
            <a-button type="primary" @click="edit">
              edit
            </a-button>

            <a-button type="danger">
              delete
            </a-button>
          </a-space>
        </span>
          </template>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
  <a-modal
      v-model:visible="modalVisible"
      title="edit"
      :confirm-loading="modalLoading"
      @ok="handleModalOk"
  >
    <p>{{ modalText }}</p>
  </a-modal>
</template>

<script lang="ts">
import { SmileOutlined, DownOutlined, } from '@ant-design/icons-vue';
import { defineComponent, ref, onMounted } from 'vue';
import axios from 'axios';




export default defineComponent({
  components: {
    SmileOutlined,
    DownOutlined,
  },
  setup() {
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize: 4,
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
        title: 'Category1',
        dataIndex: 'category1Id',
        key: 'category1Id',
      },
      {
        title: 'Category2',
        dataIndex: 'category2Id',
        key: 'category2Id',
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
      axios.get("/ebook/list", {
        params: {
          page: params.page,
          size: params.size,
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        ebooks.value = data.content.list;

        //reset pagination button
        pagination.value.current = params.page;
        pagination.value.total = data.content.total;
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

    const modalText = ref<string>('Content of the modal');
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);

    const edit = () => {
      modalVisible.value = true;
    };

    const handleModalOk = () => {
      modalText.value = 'The modal will be closed after two seconds';
      modalLoading.value = true;
      setTimeout(() => {
        modalVisible.value = false;
        modalLoading.value = false;
      }, 1000);
    };


    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      });
    });

    return {
      ebooks,
      columns,
      pagination,
      loading,
      handleTableChange,

      edit,
      modalText,
      modalVisible,
      modalLoading,
      handleModalOk,


    };
  },
});
</script>

