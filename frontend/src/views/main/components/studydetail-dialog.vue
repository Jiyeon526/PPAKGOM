<template>
  <el-dialog
    custom-class="studydetail-dialog"
    :title="selectStudy.name"
    v-model="state.dialogVisible"
    @close="handleClose"
  >
    <el-card :body-style="{ padding: '0px' }">
    <div class="image-wrapper">
      <el-image style="width: 100%; height: 190px"
        :src="'https://localhost:8443/' + selectStudy.study_thumbnail"
        :fit="fit"
        >
      </el-image>
    </div>
    <div style="text-align: left; padding: 14px;">
      <span class="title">{{ selectStudy.name }}</span>
      <div class="bottom">
        <span>{{ selectStudy.content }}</span>
      </div>
    </div>
  </el-card>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="success">입장</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<style scoped>
.studydetail-dialog {
  width: 50%;
}
.el-card {
  margin: 0 8px;
  margin-bottom: 40px;
}
.el-card .image-wrapper {
  width: 100%;
  height: 190px;
}
.el-card .title {
  width: 345px;
  display:-webkit-box;
  font-weight: bold;
  word-wrap:break-word;
  -webkit-box-orient:vertical;
  overflow:hidden;
  text-overflow:ellipsis;
  -webkit-line-clamp: 1;
}
.el-card .bottom {
  width: 345px;
  margin-top: 5px;
  display:-webkit-box;
  word-wrap:break-word;
  -webkit-box-orient:vertical;
  overflow:hidden;
  text-overflow:ellipsis;
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
