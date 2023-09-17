<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24">
        <a-col :span="8" >
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

          <a-table
              v-if="level1.length > 0"
              :columns="columns"
              :data-source="level1"
              :pagination="false"
              :loading="loading"
              size="small"
              :defaultExpandAllRows="true"
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
              <template v-if="column.key === 'name'">
                {{record.sort}} {{record.name}}
              </template>

              <template v-if="column.key === 'action'">
        <span>
          <a-space size="small">
            <a-button type="primary" @click="edit(record)" size="small">
              edit
            </a-button>
            <a-popconfirm
                title="Are you sure delete this task?"
                ok-text="Yes"
                cancel-text="No"
                @confirm="handleDelete(record.id)"
            >
              <a-button type="danger" size="small">
                delete
              </a-button>
            </a-popconfirm>

          </a-space>
        </span>
              </template>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16" >
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  save
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form layout="vertical">

            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="name"/>
            </a-form-item>

            <a-form-item>
              <a-tree-select
                  v-model:value="doc.parent"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="treeSelectData"
                  placeholder="please select parent doc"
                  tree-default-expand-all
                  :replaceFields="{label: 'name', key: 'id', value: 'id'}"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="order"/>
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="handlePreviewContent()">
                <EyeOutlined /> Content preview
              </a-button>
            </a-form-item>
            <a-form-item>
              <div id="content"></div>
            </a-form-item>

          </a-form>
        </a-col>

      </a-row>

      <a-drawer width="900" placement="right" :closable="false" :visible="drawerVisible" @close="onDrawerClose">
        <div class="wangeditor" :innerHTML="previewHtml"></div>
      </a-drawer>

    </a-layout-content>
  </a-layout>
<!--  <a-modal-->
<!--      v-model:visible="modalVisible"-->
<!--      title="doc forms"-->
<!--      :confirm-loading="modalLoading"-->
<!--      @ok="handleModalOk"-->
<!--  >-->
<!--    -->
<!--  </a-modal>-->
</template>

<script lang="ts">
import { SmileOutlined, DownOutlined, } from '@ant-design/icons-vue';
import { defineComponent, ref, onMounted, createVNode } from 'vue';
import axios from 'axios';
import { message, Modal } from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import ExclamationCircleOutlined from "@ant-design/icons-vue/ExclamationCircleOutlined";
import E from 'wangeditor'


export default defineComponent({
  name: 'AdminDoc',
  components: {
    SmileOutlined,
    DownOutlined,
  },
  setup() {
    const route = useRoute();
    console.log("router：", route);
    console.log("route.path：", route.path);
    console.log("route.query：", route.query);
    console.log("route.param：", route.params);
    console.log("route.fullPath：", route.fullPath);
    console.log("route.name：", route.name);
    console.log("route.meta：", route.meta);
    const param = ref();
    param.value = {};
    const docs = ref();
    const loading = ref(false);
    // Because the attribute status of the tree selection component
    // will change with the currently edited node, a separate responsive variable is declared.
    const treeSelectData = ref();
    treeSelectData.value = [];

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
    const level1 = ref(); // The first-level classification tree, the children attribute is the second-level classification
    level1.value = [];

    const columns = [
      {
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
        // width: 150,
      },
      {
        title: 'Action',
        key: 'action',
      },
    ];


    /**
     * Data query
     **/
    const handleQuery = () => {
      loading.value = true;
      axios.get("/doc/all/" + route.query.ebookId).then((response) => {
        loading.value = false;
        level1.value = [];
        const data = response.data;
        if(data.success){
          docs.value = data.content;
          console.log("original array：", docs.value);

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
          console.log("tree structure：", level1);

          // The parent document drop-down box is initialized, which is equivalent to clicking Add
          treeSelectData.value = Tool.copy(level1.value);
          // Add a "None" to the selection tree
          treeSelectData.value.unshift({id: 0, name: '无'});
        }
        else{
          message.error(data.message);
        }
      });

    };


    // -------- forms ---------

    const doc = ref();
    doc.value = {
      ebookId: route.query.ebookId
    };
    // const modalText = ref<string>('Content of the modal');
    const modalVisible = ref<boolean>(false);
    const modalLoading = ref<boolean>(false);
    const editor = new E('#content');
    editor.config.zIndex = 0;

    const handleSave = () => {
      // modalText.value = 'The modal will be closed after two seconds';
      modalLoading.value = true;
      doc.value.content = editor.txt.html();

      axios.post("/doc/save", doc.value).then((response) => {
        modalLoading.value = false;
        const data = response.data; //data = commonResp
        if(data.success){
          // modalVisible.value = false;
          message.success("save successful!");
          //load list again
          handleQuery();
        }else{
          message.error(data.message);
        }
      });
    };

    /**
     * Set a node and its descendant nodes to disabled
     */
    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // iterator over the array, that is, traverse the nodes of a certain layer
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // If the current node is the target node
          console.log("disabled", node);
          // Set the target node to disabled
          node.disabled = true;

          // Traverse all child nodes and add disabled to all child nodes
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          // f the current node is not the target node, go to its child nodes and look again.
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };

    const deleteIds: Array<string> = [];
    const deleteNames: Array<string> = [];
    /**
     * Find the entire branch
     */
    const getDeleteIds = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // Traverse the array, that is, traverse the nodes of a certain layer
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // If the current node is the target node
          console.log("delete", node);
          // Put target id into result set ids
          // node.disabled = true;
          deleteIds.push(id);
          deleteNames.push(node.name);

          // Traverse all child nodes
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id)
            }
          }
        } else {
          // If the current node is not the target node, go to its child nodes and look again.
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };


    /**
     * content query
     **/
    const handleQueryContent = () => {
      axios.get("/doc/find-content/" + doc.value.id).then((response) => {
        const data = response.data;
        if (data.success) {
          editor.txt.html(data.content)
        } else {
          message.error(data.message);
        }
      });
    };

    //edit
    const edit = (record: any) => {
      // clear editor
      editor.txt.html("");
      modalVisible.value = true;
      doc.value = Tool.copy(record);
      handleQueryContent();

      // You cannot select the current node and all its descendant nodes as parent nodes, which will break the tree.
      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);

      // Add a "none" to the selection tree
      treeSelectData.value.unshift({id: 0, name: 'none'});

    };

    //add
    const add = () => {
      // clear editor
      editor.txt.html("");
      modalVisible.value = true;
      doc.value = {
        ebookId: route.query.ebookId
      };

      treeSelectData.value = Tool.copy(level1.value);

      // Add a "none" to the selection tree
      treeSelectData.value.unshift({id: 0, name: 'none'});

    };

    //delete
    const handleDelete = (id: number) => {
      deleteIds.length = 0;
      deleteNames.length = 0;
      getDeleteIds(level1.value, id);
      Modal.confirm({
        title: 'Important reminder',
        icon: createVNode(ExclamationCircleOutlined),
        content: 'Will be deleted: [' + deleteNames.join("，") + "] They cannot be recovered after deletion. Confirm deletion?",
        onOk() {
          // console.log(ids)
          axios.delete("/doc/delete/" + deleteIds.join(",")).then((response) => {
            const data = response.data; // data = commonResp
            if (data.success) {
              // load list again
              handleQuery();
            }
          });
        },
      });
    };

    // ----------------Rich text preview--------------
    const drawerVisible = ref(false);
    const previewHtml = ref();
    const handlePreviewContent = () => {
      const html = editor.txt.html();
      previewHtml.value = html;
      drawerVisible.value = true;
    };
    const onDrawerClose = () => {
      drawerVisible.value = false;
    };


    onMounted(() => {
      editor.create();

      handleQuery();
    });

    return {
      param,
      // docs,
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
      handleSave,
      doc,

      treeSelectData,

      drawerVisible,
      previewHtml,
      handlePreviewContent,
      onDrawerClose,
    };
  },
});
</script>

