<template>
  <el-dialog
    custom-class="otherpeople-dialog"
    title="프로필"
    v-model="state.dialogVisible"
    @close="handleClose"
  >
    <el-row :gutter="24">
      <el-col :span="16">
        <p>이름</p>
        <p>{{state.passion}}</p>
        <el-progress :stroke-width="20" :percentage="state.passion" :show-text='false'>
        </el-progress>
      </el-col>
      <el-col :span="8">
        <el-avatar :size="50" :src="state.circleUrl"></el-avatar>
      </el-col>
    </el-row>
    <el-divider></el-divider>
    <el-row>
      <el-col>
        <p>가입된 스터디</p>
        <li v-for="std in state.studyList">
          {{ std }}
        </li>
      </el-col>
    </el-row>
    <template #footer>
      <span>1234</span>
    </template>
  </el-dialog>
</template>
<style>
.otherpeople-dialog {
  width: 500px !important;
  height: 500px;
}
.otherpeople-dialog .el-dialog__headerbtn {
  float: right;
}
.otherpeople-dialog .el-dialog__body {
  height: 330px;
}
.otherpeople-dialog .el-dialog__footer {
  border: solid;
  text-align: left;
  padding: 10px;
  margin-left: 20px;
  margin-right: 20px;
}

</style>
<script>
import { reactive, computed, ref, onMounted } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
export default {
  name: "otherpeople-dialog",

  props: {
    open: {
      type: Boolean,
      default: false
    }
  },

  setup(props, { emit }) {
    const store = useStore();
    // 마운드 이후 바인딩 될 예정 - 컨텍스트에 노출시켜야함. <return>
    const otherpeopleForm = ref(null);
    const router = useRouter();
    /*
      // Element UI Validator
      // rules의 객체 키 값과 form의 객체 키 값이 같아야 매칭되어 적용됨
      //
    */
    const state = reactive({

      dialogVisible: computed(() => props.open),
      formLabelWidth: "120px",
      passion: 50,
      studyList: [
        'study1',
        'study2'
      ],
      circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
    });

    const isDisabled = function() {
      return "disabled";
    };

    onMounted(() => {
      // console.log(otherpeopleForm.value)
    });

    const clickLogin = function() {
      // 로그인 클릭 시 validate 체크 후 그 결과 값에 따라, 로그인 API 호출 또는 경고창 표시
      loginForm.value.validate(valid => {
        if (valid) {
          console.log("submit");
          store
            .dispatch("root/requestLogin", {
              id: state.form.id,
              password: state.form.password
            })
            .then(function(result) {
              //alert("accessToken: " + result.data.accessToken);
              localStorage.setItem("accessToken", result.data.accessToken);
              localStorage.setItem("userId", state.form.id);
              ElMessage({
                message: "로그인 성공",
                type: "success"
              });
              handleClose();
              //console.log(store.getters['root/isLoggedIn'])
              loginsuccess();
            })
            .catch(function(err) {
              alert(err.message);
            });
          this.$store.state.loading = true;
        } else {
          alert("Validate error!");
        }
      });
    };

    const loginsuccess = function() {
      store.commit("root/setAccessToken");
      router.push({
        name: "home"
      });
    };

    const handleClose = function() {

      emit("closeOtherpeopleDialog")
    };

    return { otherpeopleForm, state, clickLogin, handleClose };
  }
};
</script>
