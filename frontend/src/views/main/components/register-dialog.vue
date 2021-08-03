<template>
  <el-dialog
    custom-class="register-dialog"
    title="회원가입"
    v-model="state.dialogVisible"
    @close="handleClose"
  >
    <el-form
      :model="state.form"
      :rules="state.rules"
      ref="registerForm"
      :label-position="state.form.align"
    >
      <el-form-item
        prop="department"
        label="소속"
        :label-width="state.formLabelWidth"
      >
        <el-input v-model="state.form.department" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item
        prop="position"
        label="직책"
        :label-width="state.formLabelWidth"
      >
        <el-input v-model="state.form.position" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item
        prop="name"
        label="이름"
        :label-width="state.formLabelWidth"
      >
        <el-input v-model="state.form.name" autocomplete="off"></el-input>
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="17">
          <el-form-item
            prop="id"
            label="아이디"
            :label-width="state.formLabelWidth"
          >
            <el-input v-model="state.form.id" autocomplete="off"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="checkId"
            >아이디 중복 확인</el-button
          >
        </el-col>
      </el-row>
      <el-form-item
        prop="password"
        label="비밀번호"
        :label-width="state.formLabelWidth"
      >
        <el-input
          v-model="state.form.password"
          autocomplete="off"
          show-password
        ></el-input>
      </el-form-item>

      <el-form-item
        prop="password2"
        label="비밀번호 체크"
        :label-width="state.formLabelWidth"
      >
        <el-input
          v-model="state.form.password2"
          autocomplete="off"
          show-password
        ></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="clickRegister">회원가입</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<style>
.register-dialog {
  width: 600px !important;
  height: 600px;
}
.register-dialog .el-dialog__headerbtn {
  float: right;
}
.register-dialog .el-form-item__content {
  margin-left: 0 !important;
  float: right;
  width: 200px;
  display: inline-block;
}
.register-dialog .el-form-item {
  margin-bottom: 20px;
}
.register-dialog .el-form-item__error {
  font-size: 12px;
  color: red;
}
.register-dialog .el-input__suffix {
  display: none;
}
.register-dialog .el-dialog__footer {
  margin: 0 calc(50% - 80px);
  padding-top: 0;
  display: inline-block;
}
.register-dialog .dialog-footer .el-button {
  width: 120px;
}
</style>
<script>
import { reactive, computed, ref, onMounted } from "vue";
import { useStore } from "vuex";
import { ElMessage } from "element-plus";

export default {
  name: "register-dialog",

  props: {
    open: {
      type: Boolean,
      default: false
    }
  },

  setup(props, { emit }) {
    const store = useStore();
    // 마운드 이후 바인딩 될 예정 - 컨텍스트에 노출시켜야함. <return>
    const registerForm = ref(null);

    /*
      // Element UI Validator
      // rules의 객체 키 값과 form의 객체 키 값이 같아야 매칭되어 적용됨
      //
    */
    const state = reactive({
      form: {
        department: "",
        position: "",
        name: "",
        id: "",
        password: "",
        password2: "",
        align: "left"
      },
      rules: {
        department: [
          {
            validator(rule, value) {
              var error = [];
              if (value.length >= 30) {
                error = ["최대 30자까지 입력 가능합니다."];
              }
              return error;
            },
            trigger: "blur"
          }
        ],
        position: [
          {
            validator(rule, value) {
              var error = [];
              if (value.length >= 30) {
                error = ["최대 30자까지 입력 가능합니다."];
              }
              return error;
            },
            trigger: "blur"
          }
        ],
        name: [
          { required: true, message: "Please input name", trigger: "blur" },
          {
            validator(rule, value) {
              var error = [];
              if (value.length >= 30) {
                error = ["최대 30자까지 입력 가능합니다."];
              }
              return error;
            },
            trigger: "blur"
          }
        ],
        id: [
          { required: true, message: "필수 입력 항목입니다", trigger: "blur" },
          {
            validator(rule, value) {
              var error = [];
              if (value.length >= 16) {
                error = ["최대 16자까지 입력 가능합니다."];
              }
              return error;
            },
            trigger: "blur"
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
              } else if (value.length >= 16) {
                error = ["최대 16 글자까지 입력가능합니다."];
              } else if (number < 0 || english < 0 || special < 0) {
                error = ["비밀번호는 영문, 숫자, 특수문자가 조합되어야합나다."];
              }
              return error;
            },
            trigger: "blur"
          }
        ],
        password2: [
          { required: true, message: "필수 입력 항목입니다.", trigger: "blur" },
          {
            validator(rule, value) {
              var error = [];

              if (value !== state.form.password) {
                error = ["입력한 비밀번호와 일치하지 않습니다."];
              }
              return error;
            },
            trigger: "blur"
          }
        ]
      },
      dialogVisible: computed(() => props.open),
      formLabelWidth: "130px"
    });

    onMounted(() => {
      // console.log(loginForm.value)
    });

    const checkId = function() {
      // axios 보냄
      store
        .dispatch("root/requestCheckDuplicate", { id: state.form.id })
        .then(function(result) {
          console.log(result);
          state.duplicationCheck = 1; // 성공하면 초록색
          ElMessage({
            message: "아이디 중복 검증 성공",
            type: "success"
          });
        })
        .catch(function(err) {
          console.log(err);
          state.duplicationCheck = 2; // 실패하면 빨간색
          //alert("이미 아이디가 존재합니다.");
          ElMessage({
            message: "이미 아이디가 존재합니다.",
            type: "error"
          });
        });
    };

    const clickRegister = function() {
      // 로그인 클릭 시 validate 체크 후 그 결과 값에 따라, 로그인 API 호출 또는 경고창 표시
      registerForm.value.validate(valid => {
        if (valid) {
          console.log("submit");
          store
            .dispatch("root/requestRegister", {
              department: state.form.department,
              position: state.form.position,
              name: state.form.name,
              id: state.form.id,
              password: state.form.password
            })
            .then(function(result) {
              //alert("accessToken: " + result.data.accessToken);
              //console.log(result);
              //alert(result.data.message);
              ElMessage({
                message: "회원가입성공",
                type: "success"
              });
              handleClose();
            })
            .catch(function(err) {
              alert(err);
            });
        } else {
          alert("Validate error!");
        }
      });
    };

    const handleClose = function() {
      state.form.department = "";
      state.form.position = "";
      state.form.name = "";
      state.form.id = "";
      state.form.password = "";
      state.form.password2 = "";
      emit("closeRegisterDialog");
    };

    return { registerForm, state, clickRegister, handleClose, checkId };
  }
};
</script>
