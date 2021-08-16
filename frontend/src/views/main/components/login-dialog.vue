<template>
  <el-dialog
    custom-class="login-dialog"
    title="LOGIN"
    v-model="state.dialogVisible"
    @close="handleClose"
  >
    <el-form
      :model="state.form"
      :rules="state.rules"
      ref="loginForm"
      :label-position="state.form.align"
    >
      <el-form-item
        prop="id"
        label="아이디"
        :label-width="state.formLabelWidth"
      >
        <el-input
          v-model="state.form.id"
          autocomplete="off"
          placeholder="아이디를 입력해주세요"
        ></el-input>
      </el-form-item>
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
    </el-form>
    <template #footer>
      <span>
        <span class="dialog-footer">
          <el-button type="success" @click="clickLogin" plain>로그인</el-button>
        </span>
        <br />
        <div style="display: inline-block;">
          <p>
            아직 회원이 아니신가요? &nbsp; &nbsp;

            <el-button
              type="text"
              @click="clickRegister"
              style="display:inline-block; color:green; float: rigth;"
              >회원가입</el-button
            >
          </p>
        </div>

        <div style="text-align: center;">
          <h3>간편 로그인</h3>
        </div>

        <div class="google-logo-wrapper" id="google-signin-btn"></div>
        &nbsp; &nbsp;
        <div class="kakao-logo-wrapper" @click="kakaoLogin"></div>

        <div
          class="naver-logo-wrapper"
          id="naverIdLogin"
          @click="naverlogin"
        ></div>
      </span>
      <!-- <NaverLogin
        client-id="2skX9k2csf4rw6XBSD_S"
        callback-url="http://localhost:8083/naverlogin"
        is-popup="true"
        :callbackFunction="callbackFunction"
      /> -->
    </template>
  </el-dialog>
</template>
<style>
.login-dialog {
  width: 500px !important;
  height: 500px;
}

.login-dialog .google-logo-wrapper {
  width: 120px;
  height: 40px;
  background-size: contain;
  background-repeat: no-repeat;
  /* background-image: url("../../../assets/images/google.png"); */
  display: inline-block;
}

.login-dialog .kakao-logo-wrapper {
  width: 100px;
  height: 40px;
  background-size: contain;
  background-repeat: no-repeat;
  background-image: url("../../../assets/images/kakaologin.png");
  display: inline-block;
}

.login-dialog .naver-logo-wrapper {
  width: 50px;
  height: 40px;
  background-size: contain;
  background-repeat: no-repeat;
  display: inline-block;
}
.login-dialog .el-dialog__headerbtn {
  float: right;
}
.login-dialog .el-form-item__content {
  margin-left: 0 !important;

  width: 200px;
  display: inline-block;
}
.login-dialog .el-form-item {
  margin-bottom: 20px;
}
.login-dialog .el-form-item__error {
  font-size: 12px;
  color: red;
}
.login-dialog .el-input__suffix {
  display: none;
}
.login-dialog .el-dialog__footer {
  margin: 0 calc(25% - 50px);
  padding-top: 0;
  display: inline-block;
  align-self: center;
  align-items: center;
}
.login-dialog .dialog-footer .el-button {
  width: 300px;
}
</style>
<script>
import { reactive, computed, ref, onMounted, watch } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";

export default {
  name: "login-dialog",

  props: {
    open: {
      type: Boolean,
      default: false
    }
  },

  setup(props, { emit }) {
    const store = useStore();
    // 마운드 이후 바인딩 될 예정 - 컨텍스트에 노출시켜야함. <return>
    const loginForm = ref(null);
    const router = useRouter();
    let naverLogin = null;
    /*
      // Element UI Validator
      // rules의 객체 키 값과 form의 객체 키 값이 같아야 매칭되어 적용됨
      //
    */
    const state = reactive({
      id: "",
      email: "",
      Token: "",
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
      formLabelWidth: "120px",
      flag: ""
    });

    watch(
      () => state.flag,
      () => {
        localStorage.setItem(
          "naveraccessToken",
          naverLogin.accessToken.accessToken
        );
      }
    );

    const isDisabled = function() {
      return "disabled";
    };

    onMounted(() => {
      // console.log(loginForm.value)
      //설정정보를 초기화하고 연동을 준비
      naverLogin = new window.naver.LoginWithNaverId({
        clientId: "2skX9k2csf4rw6XBSD_S", //개발자센터에 등록한 ClientID
        callbackUrl: "https://localhost:8083/", //개발자센터에 등록한 callback Url
        isPopup: false, //팝업을 통한 연동처리 여부
        loginButton: { color: "green", type: 2, height: 40 } //로그인 버튼의 타입을 지정
      });
      naverLogin.init();
      naverLogin.getLoginStatus(status => {
        if (status) {
          console.log(status);
          console.log(naverLogin.user);
          //필수적으로 받아야하는 프로필 정보가 있다면 callback처리 시점에 체크
          var email = naverLogin.user.getEmail();

          if (email == undefined || email == null) {
            alert("이메일은 필수정보입니다. 정보제공을 동의해주세요.");
            //사용자 정보 재동의를 위하여 다시 네아로 동의페이지로 이동함
            naverLogin.reprompt();
            return;
          }

          if (naverLogin.accessToken.accessToken) {
          }
        } else {
          console.log("callback 처리에 실패하였습니다.");
        }
      });
      window.gapi.signin2.render("google-signin-btn", {
        onsuccess: onSignIn
      });
    });

    const naverlogin = function() {
      //console.log("accessToken", naverLogin.accessToken.accessToken);
      if (naverLogin.accessToken.accessToken) {
        localStorage.setItem(
          "naveraccessToken",
          naverLogin.accessToken.accessToken
        );
        let naverid = naverLogin.user.getEmail().split("@");
        store
          .dispatch("root/requestSocialLogin", naverLogin.user.getEmail())
          .then(function(result) {
            alert("accessToken: " + result.data.accessToken);
            localStorage.setItem("accessToken", result.data.accessToken);
            localStorage.setItem("userId", naverid[0]);
            localStorage.setItem("userPk", result.data.id);
            store.commit("root/setUserpk", result.data.id);
            ElMessage({
              message: "로그인 성공",
              type: "success"
            });
            handleClose();
            //     //console.log(store.getters['root/isLoggedIn'])
            loginsuccess();
          })
          .catch(function(err) {
            alert(err.message);
          });
      }
      //console.log("accessToken", naverLogin.accessToken.accessToken);

      handleClose();
    };
    const kakaoLogin = function() {
      window.Kakao.Auth.login({
        scope: "profile_nickname, account_email",
        success: this.getKakaoAccount
      });

      handleClose();
    };
    const onSignIn = function(googleUser) {
      const profile = googleUser.getBasicProfile();
      console.log("ID: " + profile.getId());
      console.log("Full Name: " + profile.getName());
      console.log("Given Name: " + profile.getGivenName());
      console.log("Family Name: " + profile.getFamilyName());
      console.log("Image URL: " + profile.getImageUrl());
      console.log("Email: " + profile.getEmail());

      const id_token = googleUser.getAuthResponse().id_token;
      console.log("ID Token: " + id_token);
      console.log("전", store.getters["root/getGoogleIsLoggedIn"]);
      store.commit("root/setGoogleLogin", {
        login: true
      });
      console.log("후", store.getters["root/getGoogleIsLoggedIn"]);

      store
        .dispatch("root/requestSocialLogin", profile.getEmail())
        .then(function(result) {
          alert("accessToken: " + result.data.accessToken);
          localStorage.setItem("accessToken", result.data.accessToken);
          localStorage.setItem("userId", profile.getId());
          localStorage.setItem("userPk", result.data.id);
          store.commit("root/setUserpk", result.data.id);
          ElMessage({
            message: "로그인 성공",
            type: "success"
          });
          handleClose();
          //     //console.log(store.getters['root/isLoggedIn'])
          loginsuccess();
        })
        .catch(function(err) {
          alert(err.message);
        });
    };
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
              localStorage.setItem("userPk", result.data.id);
              store.commit("root/setUserpk", result.data.id);
              console.log("로그인정보", result.data);
              console.log("기본키", store.getters["root/getUserpk"]);
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
        } else {
          alert("Validate error!");
        }
      });
    };

    const loginsuccess = function() {
      store.commit("root/setAccessToken");
      router.push({
        name: "main"
      });
    };

    const handleClose = function() {
      state.form.id = "";
      state.form.password = "";
      emit("closeLoginDialog");
    };

    const clickRegister = function() {
      emit("closeLoginDialog");
      emit("openRegisterDialog");
    };

    const getKakaoAccount = function() {
      window.Kakao.API.request({
        url: "/v2/user/me",
        success: res => {
          const kakao_account = res.kakao_account;
          const nickname = kakao_account.profile.nickname; //카카오 닉네임
          const email = kakao_account.email; //카카오 이메일
          let kakaoid = email.split("@");
          console.log("nickname", nickname);
          console.log("email", email);
          //로그인 처리 구현
          console.log(kakao_account);
          console.log(window.Kakao.Auth.getAccessToken());

          console.log("submit");
          console.log("파싱 아이디결과", kakaoid[0]);

          console.log("전", store.getters["root/getKakaoIsLoggedIn"]);
          store.commit("root/setkakaologin", {
            login: true
          });
          console.log("후", store.getters["root/getKakaoIsLoggedIn"]);
          store
            .dispatch("root/requestSocialLogin", email)
            .then(function(result) {
              alert("accessToken: " + result.data.accessToken);
              localStorage.setItem("accessToken", result.data.accessToken);
              localStorage.setItem("userId", kakaoid[0]);
              localStorage.setItem("userPk", result.data.id);
              store.commit("root/setUserpk", result.data.id);
              ElMessage({
                message: "로그인 성공",
                type: "success"
              });
              handleClose();
              //     //console.log(store.getters['root/isLoggedIn'])
              loginsuccess();
            })
            .catch(function(err) {
              alert(err.message);
            });
          //this.$store.commit("user", kakao_account);
        },
        fail: error => {
          // this.$router.push("/errorPage");
          console.log(error);
        }
      });
    };

    return {
      loginForm,
      state,
      clickLogin,
      handleClose,
      clickRegister,
      kakaoLogin,
      getKakaoAccount,
      naverlogin,
      onSignIn
    };
  }
};
</script>
