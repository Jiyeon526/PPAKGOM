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
    <div>
      <el-input-data class="btn-sort">
        <el-tag type=""> {{ selectStudy.joined_population }}/{{ selectStudy.population }}</el-tag>
        <el-tag v-if="selectStudy.owner_id" type="success">입장</el-tag>
      </el-input-data>
    </div>
  </el-card>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="clickLogin">로그인</el-button>
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
import { reactive, computed, ref, onMounted } from "vue";
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
    const loginForm = ref(null);
    const router = useRouter();
    /*
      // Element UI Validator
      // rules의 객체 키 값과 form의 객체 키 값이 같아야 매칭되어 적용됨
      //
    */
    const state = reactive({
      form: {
        id: "",
        password: "",
        align: "left"
      },
      rules: {
        id: [
          { required: true, message: "필수 입력 항목입니다", trigger: "blur" },
          {
            validator(rule, value) {
              var error = [];
              if (value.length > 16) {
                error = ["최대 16자까지 입력 가능합니다."];
              }
              return error;
            }
          }
        ],
        password: [
          { required: true, message: "필수 입력 항목입니다.", trigger: "blur" },
          {
            validator(rule, value) {
              var error = [];
              var number = value.search(/[0-9]/g);
              var english = value.search(/[a-z]/gi);
              var special = value.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);
              if (value.length < 9) {
                error = ["최소 9글자를 입력해야 합니다."];
              } else if (value.length > 16) {
                error = ["최대 16 글자까지 입력가능합니다."];
              } else if (number < 0 || english < 0 || special < 0) {
                error = ["비밀번호는 영문, 숫자, 특수문자가 조합되어야합나다."];
              }
              return error;
            }
          }
        ]
      },
      dialogVisible: computed(() => props.open),
      formLabelWidth: "120px"
    });

    const isDisabled = function() {
      return "disabled";
    };

    onMounted(() => {
      // console.log(loginForm.value)
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
      state.form.id = "";
      state.form.password = "";
      emit("closeStudydetailDialog");
    };

    return { loginForm, state, clickLogin, handleClose };
  }
};
</script>
