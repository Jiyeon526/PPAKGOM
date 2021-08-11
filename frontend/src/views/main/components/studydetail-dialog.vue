<template>
  <el-dialog
    custom-class="studydetail-dialog"
    :title="selectStudy.name"
    v-model="state.dialogVisible"
    @close="handleClose"
  >
  <el-divider style="margin: 5px"></el-divider>
    <el-image style="width: 100%; height: 300px"
      :src="'https://localhost:8443/' + selectStudy.study_thumbnail"
      :fit="fit"
      >
    </el-image>
    <div>
      <h4>내용</h4>
      <p>{{ selectStudy.content }}</p>
    </div>
    <el-divider style="margin: 5px"></el-divider>
    <div class="detail-dialog-footer">
      <h4 style="display: inline-block">열정도 : {{ selectStudy.content }}</h4>
      <el-button type="success" plain style="height: 30px">입장</el-button>
    </div>
  </el-dialog>
</template>
<style >
.studydetail-dialog {
  width: 50%;
}
.el-dialog__header {
  padding: 20px !important
}
.el-dialog__body {
  padding: 5px 20px !important
}
.detail-dialog-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
/* 테블릿, 모바일의 경우 두 줄 말줄임표시 */
@media (max-width: 1269px) {
  .el-card .bottom {
    -webkit-line-clamp: 2;
    height:42px;
  }
}
/* 데스크탑의 경우 세 줄 말줄임표시 */
@media (min-width: 1270px) {
  .el-card .bottom {
    -webkit-line-clamp: 3;
    height:60px;
  }
}

/* 인원 왼쪽 정렬 */
.btn-sort {
  display: flex;
  justify-content: space-between;
  margin: 2px;
}
</style>
<script>
import { reactive, computed, onMounted } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
export default {
  name: "studydetail-dialog",

  props: {
    open: {
      type: Boolean,
      default: false
    },
    selectStudy: {
      type: Object,
    }
  },

  setup(props, { emit }) {
    const store = useStore();
    // 마운드 이후 바인딩 될 예정 - 컨텍스트에 노출시켜야함. <return>
    const router = useRouter();
    /*
      // Element UI Validator
      // rules의 객체 키 값과 form의 객체 키 값이 같아야 매칭되어 적용됨
      //
    */
    const state = reactive({
      dialogVisible: computed(() => props.open),
    });

    onMounted(() => {
      // console.log(loginForm.value)
    });

    const handleClose = function() {
      emit("closeStudydetailDialog");
    };

    return { state, handleClose };
  }
};
</script>
