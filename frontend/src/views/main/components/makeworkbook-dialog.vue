<template>
  <el-dialog
    custom-class="makeworkbook-dialog"
    :title="workbookInfo.title"
    v-model="state.dialogVisible"
    @close="handleClose"
  >
    <el-row :gutters="24">
      <el-col :span="13" class="pdfCol">
        <button :disabled="state.page <= 1" @click="state.page--">❮</button>
        {{ state.page }} / {{ state.pageCount }}
      <button :disabled="state.page >= state.pageCount" @click="state.page++">❯</button>

      <vue-pdf-embed
        style="height:80%"
        ref="pdfRef"
        :page = state.page
        :source= "state.uri"
        @rendered="handleRender" />
    </el-col>
    <el-col :span="1"></el-col>
    <el-col :span="10">
      <el-table
        height="600"
        :data="state.tableData">
        <el-table-column
          label="NO."
          type="index">
        </el-table-column>
        <el-table-column prop="answer" label="Answer">
          <template #default="scope">
              <el-input size="small"
                v-model="scope.row.answer" controls-position="right"></el-input>
          </template>
        </el-table-column>
      </el-table>

      <el-button style="margin-top:20px" @click="handleClick">submit</el-button>
    </el-col>
  </el-row>
  </el-dialog>
</template>
<script>
import { reactive, computed, ref, onMounted, watch } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import VuePdfEmbed from "vue-pdf-embed";
import axios from "axios";

export default {
  name: "makeworkbook-dialog",

  components: {
    VuePdfEmbed
  },

  props: {
    open: {
      type: Boolean,
      default: false
    },
    workbookInfo: {
      type: Object
    },
    problemCnt: {
      type: Number
    }
  },

  setup(props, { emit }) {
    const store = useStore();
    const answerbookForm = ref(null);
    const pdfUpload = ref(null);
    const pdfRef = ref(null);
    const router = useRouter();

    const state = reactive({
      form: {
        title: "",
        align: "left"
      },
      dialogVisible: computed(() => props.open),
      formLabelWidth: "120px",
      page: 1,
      pageCount: 1,
      tableCount: 0,
      tableData: computed(() => store.getters["root/getproblemCnt"]),
      uri: "",
      studyData: ""
    });

    watch(()=>props.problemCnt,()=>{
      store.commit('root/setproblemCnt', props.problemCnt)
      // for (let i=0; i < props.problemCnt; i++ ){
      //   state.tableData.push({'answer': ""})
      // }
    })

    watch(()=>props.workbookInfo,()=>{
      console.log(props.workbookInfo)
      state.studyData = props.workbookInfo.test_url;

      console.log(state.studyData);
      var name;
      if (
        state.studyData.split("\\").length > state.studyData.split("/").length
      ) {
        name = state.studyData.split("\\");
      } else {
        name = state.studyData.split("/");
      }
    }
    );


    const isDisabled = function() {
      return "disabled";
    };

    onMounted(() => {
      // state.pagecount = pdfRef.value.pagecount
      console.log(props.problemCnt);
    });

    const handleClose = function() {
      console.log(props.problemCnt)
      console.log(state.tableData)
      props.problemCnt = 0,
      console.log(props.problemCnt)
      state.page = 1,
      state.pageCount = 1,
      state.tableData = [],
      emit("closeMakeworkbookDialog");
    };

    const handleRender = function() {
      state.pageCount = pdfRef.value.pageCount;
    };

    const handleClick = function() {
      console.log(state.tableData)
      const newtab = []
      for(let val in state.tableData) {
        newtab.push(state.tableData[val]["answer"])
      }
      store.commit('root/setTestpk', props.workbookInfo.id)
      store.dispatch('root/requestSubmitAnswer', {
        answer: newtab
      })
      .then(function(res) {
        ElMessage({
          message: "문제 수: " + res.data["number"]+', ' + "맞은 문제 개수:" + res.data["correct"],
          type: "success"
        })
        .then(function(res) {
          console.log(res);
          ElMessage({
            message: res.data,
            type: "success"
          });
        })
        .catch(function(err) {
          console.log(err);
        });
    })
    }

    return {
      answerbookForm,
      pdfUpload,
      pdfRef,
      state,
      handleClose,
      handleRender,
      handleClick
    };
  }
}
</script>

<style>
.makeworkbook-dialog {
  height: 800px;
  width: 800px;
}
.numbering {
  height: 400px;
  border: solid;
}
.pdfCol {
  height: 700px;
  border: solid;
}
</style>
