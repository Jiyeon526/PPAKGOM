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
      <el-row :gutter="20">
        <el-col :span="18">
          <el-form-item
            prop="id"
            label="아이디"
            :label-width="state.formLabelWidth"
          >
            <el-input
              v-model="state.form.id"
              autocomplete="off"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="checkId">아이디 중복</el-button>
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
        label="비밀번호 재확인"
        :label-width="state.formLabelWidth"
      >
        <el-input
          v-model="state.form.password2"
          autocomplete="off"
          show-password
        ></el-input>
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="18">
          <el-form-item
            prop="email"
            label="이메일"
            :label-width="state.formLabelWidth"
          >
            <el-input
              placeholder="Ex) example@naver.com"
              v-model="state.form.email"
              autocomplete="off"
              clearable
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="checkEmail">이메일 인증</el-button>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="18">
          <el-form-item
            prop="checkcode"
            label="인증코드"
            :label-width="state.formLabelWidth"
          >
            <el-input
              v-model="state.form.checkcode"
              autocomplete="off"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="checkCode">인증코드 확인</el-button>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="18">
          <el-form-item
            prop="name"
            label="닉네임"
            :label-width="state.formLabelWidth"
          >
            <el-input v-model="state.form.name" autocomplete="off" clearable>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="checkName">닉네임 중복</el-button>
        </el-col>
      </el-row>

      <el-form-item
        prop="interet"
        label="관심사항"
        :label-width="state.formLabelWidth"
      >
        <!-- <el-input
          placeholder="Ex) #관심사항1 #관심사항2"
          v-model="state.form.interest"
          autocomplete="off"
          clearable
        ></el-input> -->
        <el-tag
          :key="tag"
          v-for="tag in state.dynamicTags"
          closable
          :disable-transitions="false"
          @close="taghandleClose(tag)"
        >
          {{ tag }}
        </el-tag>
        <el-input
          class="input-new-tag"
          v-if="state.inputVisible"
          v-model="state.inputValue"
          ref="saveTagInput"
          size="mini"
          @keyup.enter="handleInputConfirm"
          @blur="handleInputConfirm"
        >
        </el-input>
        <el-button v-else class="button-new-tag" size="small" @click="showInput"
          >+ New Tag</el-button
        >
      </el-form-item>

      <el-form-item
        prop="thumbnail"
        label="프로필 사진"
        :label-width="state.formLabelWidth"
      >
        <el-upload
          class="upload-demo"
          action="https://jsonplaceholder.typicode.com/posts/"
          accept=".png, .jpg, .jpeg, .gif"
          list-type="picture"
          limit="1"
          :before-upload="prevUpload"
          :auto-upload="false"
          thumbnail-mode="true"
          ref="toUpload"
        >
          <i class="el-icon-plus"></i>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button type="primary" @click="clickRegister">가입하기</el-button>
      </span>
    </template>
  </el-dialog>
</template>
<style>
.register-dialog {
  width: 600px !important;
  height: 750px;
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
  margin: 0 calc(20% - 80px);
  padding-top: 0;
  display: inline-block;
}
.register-dialog .dialog-footer .el-button {
  width: 500px;
}
.register-dialog .el-tag + .el-tag {
  margin-left: 10px;
}
.register-dialog .button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.register-dialog .input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
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
    const toUpload = ref(null);
    const reader = new FileReader();
    /*
      // Element UI Validator
      // rules의 객체 키 값과 form의 객체 키 값이 같아야 매칭되어 적용됨
      //
    */
    const state = reactive({
      form: {
        email: "",
        position: "",
        name: "",
        id: "",
        password: "",
        password2: "",
        align: "left",
        interest: "",
        thumbnail: [],
        checkcode: ""
      },
      rules: {
        email: [
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
      formLabelWidth: "130px",
      dynamicTags: [],
      inputVisible: false,
      inputValue: "",
      uploading: [],
      fileList: [
        {
          name: "food.jpeg",
          url:
            "https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100"
        }
      ]
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
      toUpload.value.submit();
      registerForm.value.validate(valid => {
        if (valid) {
          console.log("submit");
          // var split = state.form.interest.split("#");
          // var interesting = [];
          // split.forEach(e => {
          //   var temp = e.trim();
          //   if (temp != "") {
          //     interesting.push(temp);
          //   }
          // });
          // console.log(interesting);

          let body = new FormData();
          body.append("file", state.uploading);
          body.append("email", state.form.enail);
          body.append("interest", state.dynamicTags);
          body.append("name", state.form.name);
          body.append("password", state.form.password);
          body.append("userId", state.form.id);
          console.log(body);
          console.log(state.uploading);
          console.log(state.dynamicTags);

          store
            .dispatch("root/requestRegister", body)
            .then(function(result) {
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
      state.form.email = "";
      state.form.position = "";
      state.form.name = "";
      state.form.id = "";
      state.form.password = "";
      state.form.password2 = "";
      emit("closeRegisterDialog");
    };

    const checkEmail = function() {
      // axios 보냄
      store
        .dispatch("root/requestEmail", state.form.email)
        .then(function(result) {
          console.log(result);
          state.duplicationCheck = 1; // 성공하면 초록색
          ElMessage({
            message: "이메일 인증코드를 입력하세요",
            type: "success"
          });
        })
        .catch(function(err) {
          console.log(err);
          state.duplicationCheck = 2; // 실패하면 빨간색
          //alert("이미 아이디가 존재합니다.");
          ElMessage({
            message: "이메일 인증보내기가 실패하였습니다",
            type: "error"
          });
        });
    };

    const checkCode = function() {
      // axios 보냄
      store
        .dispatch("root/requestEmailCode", {
          email: state.form.email,
          code: state.form.checkcode
        })
        .then(function(result) {
          console.log(result);
          state.duplicationCheck = 1; // 성공하면 초록색
          ElMessage({
            message: "이메일 인증 성공",
            type: "success"
          });
        })
        .catch(function(err) {
          console.log(err);
          state.duplicationCheck = 2; // 실패하면 빨간색
          //alert("이미 아이디가 존재합니다.");
          ElMessage({
            message: "이메일 인증 실패",
            type: "error"
          });
        });
    };

    const checkName = function() {
      // axios 보냄
      store
        .dispatch("root/requestCheckDuplicate", { name: state.form.name })
        .then(function(result) {
          console.log(result);
          state.duplicationCheck = 1; // 성공하면 초록색
          ElMessage({
            message: "유일한 닉네임!",
            type: "success"
          });
        })
        .catch(function(err) {
          console.log(err);
          state.duplicationCheck = 2; // 실패하면 빨간색
          //alert("이미 아이디가 존재합니다.");
          ElMessage({
            message: "닉네임 중복",
            type: "error"
          });
        });
    };

    // 썸네일 데이터 가져오기
    const prevUpload = function(file) {
      state.uploading = file;
    };

    const taghandleClose = function(tag) {
      state.dynamicTags.splice(state.dynamicTags.indexOf(tag), 1);
    };

    const showInput = function() {
      state.inputVisible = true;
      // $nextTick(_ => {
      //   $refs.saveTagInput.$refs.input.focus();
      // });
    };

    const handleInputConfirm = function() {
      let inputValue = state.inputValue;
      if (inputValue) {
        state.dynamicTags.push(inputValue);
      }
      state.inputVisible = false;
      state.inputValue = "";
    };

    return {
      registerForm,
      state,
      clickRegister,
      handleClose,
      checkId,
      checkEmail,
      checkName,
      prevUpload,
      toUpload,
      checkCode,
      taghandleClose,
      showInput,
      handleInputConfirm
    };
  }
};
</script>
