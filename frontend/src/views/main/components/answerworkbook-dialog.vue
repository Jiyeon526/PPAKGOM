<template>
  <el-dialog
    custom-class="answerworkbook-dialog"
    title="문제집 만들기"
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
        :source="state.src"
        @rendered="handleRender" />
    </el-col>
    <el-col :span="1"></el-col>
    <el-col :span="10">
      <el-form
        :model="answerbookForm"
        :rules="rules"
        ref="answerbookForm">
        <el-form-item label="제목" prop="title">
          <el-input v-model="state.form.title" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item
          label="파일 업로드"
          prop="uploading"
        >
          <el-upload
            action="https://jsonplaceholder.typicode.com/posts/"
            accept=".pdf"
            :on-change="fileChange"
            :auto-upload="false"
            limit="1"
            ref="pdfUpload"
            :on-remove="handleRemove"
          >
          <el-button type="primary">Upload</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button @click="addRow">Add</el-button>
          <el-button @click="saveAll">Save All</el-button>

          <el-table
            height="400"
            :data="state.tableData">
            <el-table-column
              label="NO."
              type="index">
            </el-table-column>
            <el-table-column prop="answer" label="Answer">
              <template #default="scope">
                  <el-input size="small"
                    style="text-align:center"
                    v-model="scope.row.answer" controls-position="right"></el-input>
              </template>
            </el-table-column>
            <el-table-column align='right' width="50" >
              <template #default="scope">
                <el-button icon="el-icon-circle-close" @click="deleteRow(scope.$index, scope.row)" type="text" size="small">
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
        <el-form-item align='right'>
          <el-button type="primary" >만들기</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
  </el-dialog>
</template>
<script>
import { reactive, computed, ref, onMounted } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import VuePdfEmbed from 'vue-pdf-embed';

export default {
  name: "answerworkbook-dialog",

  components: {
      VuePdfEmbed,
    },

  props: {
    open: {
      type: Boolean,
      default: false
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
        uploading: [],
        align: "left"
      },
      src: "https://cdn.rawgit.com/mozilla/pdf.js/c6e8ca86/test/pdfs/freeculture.pdf",
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
      // console.log(loginForm.value)
    });

    const handleClose = function() {

      emit("closeAnswerWorkbookDialog");
    };

    const fileChange = function(file) {
      const necessary = []
      necessary.push(file['name'])
      necessary.push(file['size'])
      state.form.uploading = necessary
      state.form.uploading = file.raw
      console.log('111', file)
      console.log('222', state.form.uploading)
    }


    const handleRemove = function(file, fileList) {
        console.log(file, fileList)
    }

    const handleRender = function() {
      state.pageCount = pdfRef.value.pageCount
    }

    const addRow = function() {
      const newRow = {}
      state.tableData = [...state.tableData,newRow]
      ++ state.tableCount
      console.log("click!",state.tableCount)
      console.log("click2!",state.tableData)

    }
    const deleteRow = function(index,row) {
        state.tableData.splice(index, 1);
        if(state.tableCount > 0)
          -- state.tableCount;
    }

    return { answerbookForm, pdfUpload, pdfRef, state, handleClose, handleRender, addRow, deleteRow, fileChange };
  }
};
</script>

<style>
.answerworkbook-dialog {
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
