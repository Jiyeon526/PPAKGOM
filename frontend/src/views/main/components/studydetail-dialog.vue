<template>
  <el-dialog
    width="30%"
    :title="selectStudy.name"
    v-model="state.dialogVisible"
    @close="handleClose"
  >
  <div v-if="!selectStudy.enter" style="display:flex; justify-content:flex-end" >
    <el-button v-if="state.likeStudy" style="border:0; outline:0" icon="el-icon-star-off" @click="clickLikeBtn"></el-button>
    <el-button v-else style="border:0; outline:0" icon="el-icon-star-on" @click="clickLikeCancleBtn"></el-button>
  </div>
  <el-divider style="margin: 5px"></el-divider>
    <el-image style="width: 80%; height: 80%; display:block; margin:0px auto;"
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
      <h4 style="display: inline-block">열정도 : {{ selectStudy.temperature }}</h4>
      <el-button v-if="selectStudy.enter" type="success" plain style="height: 30px" @click="enterStudy">입장</el-button>
      <el-button v-else type="success" plain style="height: 30px" @click="requestJoinStudy">가입</el-button>
    </div>
  </el-dialog>
</template>
<style>
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
      likeStudy: false,
    });

    onMounted(() => {
      // console.log(loginForm.value)
    });

    const enterStudy = () => {
    store.commit("root/setStudypk", props.selectStudy.study_id);
    store.commit("root/setSelectOption", "studyhome");
    handleClose()
    router.push({
      name: 'studyhome'
    });
  }

    const requestJoinStudy = () => {
      store
        .dispatch('root/requestJoinStudy', {
          studyId: props.selectStudy.study_id
        })
          .then(function(res) {
            console.log("가입 요청 성공", res)
            ElMessage({
              type: "success",
              message: "가입 요청을 보냈습니다."
            })
          })
          .catch(function(err) {
            console.log("가입 신청 오류 발생!!", err)
          })
    }

    const clickLikeBtn = () => {
      state.likeStudy = !state.likeStudy
      store
        .dispatch('root/requestLikeStudy', {
          studyId: props.selectStudy.study_id
        })
          .then(function(res) {
            ElMessage({
              type: "success",
              message: `${props.selectStudy.name} 스터디를 찜하였습니다.`
            })
          })
          .catch(function(err) {
            console.log("찜하기 에러", err)
          })
      console.log("찜하기", state.likeStudy)
    }

    const clickLikeCancleBtn = () => {
      state.likeStudy = !state.likeStudy
      store
        .dispatch('root/requestLikeCancleStudy', {
          studyId: props.selectStudy.study_id
        })
          .then(function(res) {
            ElMessage({
              type: "success",
              message: `${props.selectStudy.name} 스터디를 찜하기를 취소하였습니다.`
            })
          })
          .catch(function(err) {
            console.log("찜하기 에러", err)
          })
      console.log("찜하기취소", state.likeStudy)
    }

    const handleClose = function() {
      emit("closeStudydetailDialog");
    };

    return { state, handleClose, enterStudy, requestJoinStudy, clickLikeBtn, clickLikeCancleBtn };
  }
};
</script>
