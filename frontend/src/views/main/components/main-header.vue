<template>
  <el-row class="main-header" :gutter="10" :style="{ height: height }">
    <div class="hide-on-small">
      <div class="logo-wrapper" @click="clickLogo">
        <div class="ic ic-logo" />
      </div>
      <div class="tool-wrapper">
        <div class="button-wrapper">
          <div
            v-if="
              state.isLoggedIn ||
                state.googleislogin ||
                state.kakaoislogin ||
                state.isNaverLoggedIn
            "
          >
            <!-- <el-button @click="clickLogout">로그아웃</el-button> -->

            <el-button
              type="primary"
              icon="el-icon-switch-button"
              @click="clickLogout"
              >로그아웃</el-button
            >
            <el-button type="primary" @click="clickMypage">프로필</el-button>
          </div>
          <div v-else>
            <el-button type="primary" @click="clickRegister"
              >회원가입</el-button
            >
            <el-button type="primary" @click="clickLogin">로그인</el-button>
          </div>
        </div>
      </div>
    </div>
    <div class="hide-on-big">
      <div class="menu-icon-wrapper" @click="changeCollapse">
        <i class="el-icon-menu"></i>
      </div>
      <div class="logo-wrapper" @click="clickLogo">
        <div class="ic ic-logo" />
      </div>
      <div class="mobile-sidebar-wrapper" v-if="!state.isCollapse">
        <div class="mobile-sidebar">
          <div class="mobile-sidebar-tool-wrapper">
            <div class="logo-wrapper"><div class="ic ic-logo" /></div>

            <div v-if="state.isLoggedIn">
              <el-button class="mobile-sidebar-btn" @click="clickLogout"
                >로그아웃</el-button
              >
              <el-button
                class="mobile-sidebar-btn"
                type="primary"
                @click="clickMypage"
                >프로필</el-button
              >
            </div>
            <div v-else>
              <el-button
                type="primary"
                class="mobile-sidebar-btn login-btn"
                @click="clickLogin"
                >로그인</el-button
              >
              <el-button
                type="primary"
                class="mobile-sidebar-btn register-btn"
                @click="clickRegister"
                >회원가입</el-button
              >
            </div>
          </div>
          <el-menu
            :default-active="String(state.activeIndex)"
            active-text-color="#ffd04b"
            class="el-menu-vertical-demo"
            @select="menuSelect"
          >
            <el-menu-item
              v-for="(item, index) in state.menuItems"
              :key="index"
              :index="index.toString()"
            >
              <i v-if="item.icon" :class="['ic', item.icon]" />
              <span>{{ item.title }}</span>
            </el-menu-item>
          </el-menu>
        </div>
        <div class="mobile-sidebar-backdrop" @click="changeCollapse"></div>
      </div>
    </div>
  </el-row>
</template>
<script>
import { reactive, computed } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import axios from "axios";
export default {
  name: "main-header",

  props: {
    height: {
      type: String,
      default: "70px"
    }
  },

  setup(props, { emit }) {
    const store = useStore();
    const router = useRouter();

    const state = reactive({
      isCollapse: true,
      isLoggedIn: computed(() => store.getters["root/isLoggedIn"]),
      isNaverLoggedIn: computed(() => store.getters["root/isNaverLoggedIn"]),
      userId: computed(() => store.getters["root/userId"]),
      naverToken: computed(() => store.getters["root/getNaverAccessToken"]),
      kakaoislogin: computed(() => store.getters["root/getKakaoIsLoggedIn"]),
      googleislogin: computed(() => store.getters["root/getGoogleIsLoggedIn"]),
      menuItems: computed(() => {
        const MenuItems = store.getters["root/getMenus"];
        let keys = Object.keys(MenuItems);
        let menuArray = [];
        if (!state.isLoggedIn) {
          let menuObject = {};
          menuObject.icon = MenuItems[keys[0]].icon;
          menuObject.title = MenuItems[keys[0]].name;
          menuArray.push(menuObject);

          return menuArray;
        }

        for (let i = 0; i < keys.length; ++i) {
          let menuObject = {};
          menuObject.icon = MenuItems[keys[i]].icon;
          menuObject.title = MenuItems[keys[i]].name;
          menuArray.push(menuObject);
        }
        return menuArray;
      }),
      activeIndex: computed(() => store.getters["root/getActiveMenuIndex"])
    });

    if (state.activeIndex === -1) {
      state.activeIndex = 0;
      store.commit("root/setMenuActive", 0);
    }

    const menuSelect = function(param) {
      store.commit("root/setMenuActive", param);
      const MenuItems = store.getters["root/getMenus"];
      let keys = Object.keys(MenuItems);
      router.push({
        name: keys[param]
      });
    };

    const clickLogo = () => {
      store.commit("root/setMenuActive", 0);
      const MenuItems = store.getters["root/getMenus"];
      let keys = Object.keys(MenuItems);
      router.push({
        name: keys[0]
      });
    };

    const clickLogin = () => {
      emit("openLoginDialog");
    };
    const clicktestanswer = () => {
      emit("OpenAnswerworkbookDialog");
    };

    const clickLogout = () => {
      store.dispatch("root/requestLogout");
      store.commit("root/deleteToken");

      if (state.isNaverLoggedIn) {
        const accessToken = state.naverToken;
        console.log("logout accessToken", accessToken);
        const url = `https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=2skX9k2csf4rw6XBSD_S&client_secret=MFJnBhWD3K&access_token=${accessToken}&service_provider=NAVER`;
        axios.get(url).then(res => {
          console.log("결과", res.data);
          store.dispatch("root/requestNaverLogout");
          store.commit("root/deleteNaverToken");
          console.log(state.isNaverLoggedIn);
        });
      }

      if (state.kakaoislogin) {
        if (!window.Kakao.Auth.getAccessToken()) {
          console.log("Not logged in.");
          return;
        }
        window.Kakao.Auth.logout(response => {
          //로그아웃
          console.log("access token:", window.Kakao.Auth.getAccessToken());
          console.log("log out:", response);
        });
        store.commit("root/setkakaologin", {
          login: false
        });
      }

      if (state.googleislogin) {
        window.gapi.auth2.getAuthInstance().disconnect();
        store.commit("root/setGoogleLogin", {
          login: false
        });
      }
      router.push({
        name: "main"
      });
    };

    const clickRegister = () => {
      emit("openRegisterDialog");
    };

    const clickRoomCreation = () => {
      emit("openRoomDialog");
    };

    const clickMypage = () => {
      store.commit("root/setUserId");
      router.push({
        name: "mypage",
        params: {
          userId: state.userId
        }
      });
    };

    const changeCollapse = () => {
      state.isCollapse = !state.isCollapse;
    };

    return {
      state,
      menuSelect,
      clickLogo,
      clickLogin,
      clickLogout,
      clickMypage,
      clickRegister,
      changeCollapse,
      clickRoomCreation,
      clicktestanswer
    };
  }
};
</script>
<style>
.main-header {
  padding: 10px 20px;
}
/*Mobile, Tablet*/
.menu-icon-wrapper {
  display: inline-block;
  vertical-align: top;
  position: relative;
  top: 14px;
}

.main-header .hide-on-big .logo-wrapper {
  display: inline-block;
  margin: 0 calc(50% - 51px);
}
.main-header .hide-on-big .logo-wrapper .ic.ic-logo {
  width: 70px;
  height: 50px;
  background-size: contain;
  background-repeat: no-repeat;
  background-image: url("../../../assets/images/ppakgom.png");
}
.mobile-sidebar-wrapper {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}
.mobile-sidebar-wrapper .mobile-sidebar {
  width: 240px;
  height: calc(100vh - 1px);
  display: inline-block;
  background-color: white;
  padding: 0 10px;
  vertical-align: top;
}
.mobile-sidebar-wrapper .mobile-sidebar .mobile-sidebar-tool-wrapper {
  padding-bottom: 20px;
}
.mobile-sidebar-wrapper .mobile-sidebar .mobile-sidebar-btn {
  display: block;
  margin: 0 auto;
  margin-top: 25px;
  height: 30px;
  width: 100%;
}
.mobile-sidebar-wrapper .mobile-sidebar .mobile-sidebar-btn.login-btn {
  color: white;
}
.mobile-sidebar-wrapper .mobile-sidebar .logo-wrapper {
  display: block;
}
.mobile-sidebar-wrapper .mobile-sidebar .logo-wrapper .ic.ic-logo {
  width: 70px;
  height: 50px;
  margin: 0 auto;
  margin-top: 30px;
  background-size: contain;
  background-repeat: no-repeat;
  background-image: url("../../../assets/images/ppakgom.png");
}
.mobile-sidebar-wrapper .mobile-sidebar-backdrop {
  width: calc(100% - 260px);
  height: calc(100vh - 1px);
  background-color: black;
  display: inline-block;
  opacity: 0.3;
}
.mobile-sidebar-wrapper .el-menu {
  margin-top: 0;
  padding-left: 0;
  height: calc(100% - 235px);
}
.mobile-sidebar-wrapper .el-menu .el-menu-item {
  cursor: pointer;
}
.mobile-sidebar-wrapper .el-menu .el-menu-item .ic {
  margin-right: 5px;
}

/*Desktop - Need to add Class if Need*/
.main-header .hide-on-small .logo-wrapper {
  cursor: pointer;
  display: inline-block;
}
.main-header .hide-on-small .logo-wrapper .ic.ic-logo {
  width: 70px;
  height: 50px;
  background-size: contain;
  background-repeat: no-repeat;
  background-image: url("../../../assets/images/ppakgom.png");
}
.main-header .hide-on-small .tool-wrapper {
  width: 50%;
  float: right;
}
.main-header .hide-on-small .tool-wrapper .button-wrapper {
  width: 45%;
  float: right;
}
.main-header .hide-on-small .tool-wrapper .button-wrapper .el-button {
  width: 45%;
  height: 50px;
  cursor: pointer;
  margin-right: 1%;
}

.main-header
  .hide-on-small
  .tool-wrapper
  .el-input
  .el-input__inner {
  /* width: 88%; */
  height: 50px;
  margin-right: 1%;
}
.main-header
  .hide-on-small
  .tool-wrapper
  .el-input
  .el-input__prefix {
  top: 5px;
}
</style>
