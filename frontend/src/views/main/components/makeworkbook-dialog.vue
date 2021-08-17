<template>
  <el-dialog
    custom-class="makeworkbook-dialog"
    :title= workbookInfo.title
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
        :source= "'https://localhost:8443/' + workbookInfo.test_url"
        @rendered="handleRender" />
    </el-col>
    <el-col :span="1"></el-col>
    <el-col :span="10">
      <el-input v-for="cnt in problemCnt" :key="cnt" v-model="state.tableData[cnt-1]"
      ></el-input>
      <el-button @click="handleClick">submit</el-button>
    </el-col>
  </el-row>
  </el-dialog>
</template>
<script>
import { reactive, computed, ref, onMounted, watch } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import VuePdfEmbed from 'vue-pdf-embed';

export default {
  name: "makeworkbook-dialog",

  components: {
      VuePdfEmbed,
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
    // 마운드 이후 바인딩 될 예정 - 컨텍스트에 노출시켜야함. <return>
    const answerbookForm = ref(null);
    const pdfUpload = ref(null);
    const pdfRef = ref(null);
    const router = useRouter();
    /*
      // Element UI Validator
      // rules의 객체 키 값과 form의 객체 키 값이 같아야 매칭되어 적용됨
      //
    */
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
      tableData: [],
    });



    const isDisabled = function() {
      return "disabled";
    };

    onMounted(() => {
      // state.pagecount = pdfRef.value.pagecount
      console.log(props.problemCnt)
    });

    const handleClose = function() {
      console.log(state.tableData)
      emit("closeMakeworkbookDialog");
    };

    const handleRender = function() {
      state.pageCount = pdfRef.value.pageCount
    }

    const handleClick = function() {
      console.log(state.tableData)
      const newtab = []
      for(let val in state.tableData) {
        console.log([state.tableData[val]])
        newtab.push(state.tableData[val])
      }
      store.commit('root/setTestpk', props.workbookInfo.id)
      console.log(props.workbookInfo)
      console.log(newtab)
      // let body = new FormData()
      // body.append("answer",newtab)
      // store.dispatch('root/requestSubmitAnswer',body)
      store.dispatch('root/requestSubmitAnswer', {
        answer:JSON.stringify(newtab)
      })
      .then(function(res) {
        console.log(res)
      })
      .catch(function(err) {
        console.log(err)
      })
    }


    return { answerbookForm, pdfUpload, pdfRef, state, handleClose, handleRender, handleClick };
  }
};
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
  border: solid
}
</style>
