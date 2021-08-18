<template>
  <el-row class="main-header" :gutter="10" :style="{ height: height }">
    <div class="hide-on-small">
      <el-row align="middle">
        <el-col :span="1">
          <div class="logo-wrapper" @click="clickLogo">
            <div class="ic ic-logo"></div>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="logo-ppakgom" @click="clickLogo">PPAKGOM</div>
        </el-col>

        <el-col :span="19">
          <div class="button-wrapper" style="float:right;">
            <div
              style="display:inline-block;"
              v-if="
                state.isLoggedIn ||
                  state.googleislogin ||
                  state.kakaoislogin ||
                  state.isNaverLoggedIn
              "
            >
              <span style="display:inline-block;  " v-if="state.studyPk">
                <el-dropdown trigger="click" Button>
                  <span>
                    <el-button class="el-dropdown-link" plain type="success">
                      <i :class="state.icon"></i> {{ state.label }}
                      <i class="el-icon-arrow-down"></i
                    ></el-button>
                  </span>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item
                        v-for="item in state.options"
                        :key="item.value"
                        @click="gotodetail(item)"
                        ><i :class="item.icon"></i>
                        {{ item.label }}</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </span>
              <span style="display:inline-block;">
                <el-popover
                  placement="bottom"
                  :width="300"
                  trigger="click"
                  :visible="state.visible"
                >
                  <template #reference>
                    <div style="margin:5px; padding:5px;">
                      <el-badge
                        class="item"
                        :value="state.length"
                        :max="99"
                        type="warning"
                      >
                        <el-button
                          icon="el-icon-chat-round"
                          type="success"
                          plain
                          @click="reversea"
                        >
                        </el-button>
                      </el-badge>
                    </div>
                  </template>
                  <div style="height:300px; overflow:scroll;">
                    <!-- <input
                      v-model="state.message"
                      type="text"
                      @keyup="sendMessagePub"
                    /> -->
                    <div v-for="(item, idx) in state.recvList" :key="idx">
                      <div v-if="item.writer != state.userId">
                        <span class="otherchatbox">
                          {{ item.conference_id }}번방 : {{ item.message }}
                        </span>
                      </div>
                    </div>
                  </div>
                </el-popover>
              </span>
              <el-button type="success" plain @click="clickMypage"
                ><i class="el-icon-s-custom"></i> 프로필</el-button
              >
              <el-button
                style="display:inline-block; flex:right;"
                type="success"
                plain
                icon="el-icon-switch-button"
                @click="clickLogout"
              >
                로그아웃</el-button
              >
            </div>
            <div style="display:inline-block;" v-else>
              <!-- <el-button type="primary" @click="clickOtherpeoplepage">타프로필</el-button>
            <el-button type="primary" @click="clickMypage">프로필</el-button> -->
              <el-button type="success" plain @click="clickRegister"
                ><i class="el-icon-connection"></i> 회원가입</el-button
              >
              <el-button type="success" plain @click="clickLogin"
                ><i class="el-icon-user"></i> 로그인</el-button
              >
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </el-row>
</template>
<script>
import { reactive, computed, watch, onUnmounted, onMounted } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import axios from "axios";
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
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
      studyPk: computed(() => store.getters["root/getStudypk"]),
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
      activeIndex: computed(() => store.getters["root/getActiveMenuIndex"]),
      options: [
        {
          value: "studyhome",
          label: "메인 홈",
          icon: "el-icon-s-home"
        },
        {
          value: "studyworkbook",
          label: "문제모음",
          icon: "el-icon-reading"
        },
        {
          value: "studyscore",
          label: "점수차트",
          icon: "el-icon-s-marketing"
        },
        {
          value: "studyattitude",
          label: "출석현황",
          icon: "el-icon-s-claim"
        },
        {
          value: "studyschedule",
          label: "일정관리",
          icon: "el-icon-date"
        },
        {
          value: "studymeeting",
          label: "화상회의",
          icon: "el-icon-video-camera-solid"
        },
        {
          value: "studymember",
          label: "맴버관리",
          icon: "el-icon-user-solid"
        }
      ],
      value: "", // computed는 readonly이기 때문에 이후에 option에서 value값을 바꿔도 바뀌지 않음 -> 변수를 나누어 2개로 설정
      value2: computed(() => store.getters["root/getSelectOption"]),
      label: "STUDY",
      length: 0,
      visible: false,
      icon: "",
      joinlist: computed(() => store.getters["root/getJoinStudyList"]),
      stompClient: "",
      connected: false,
      recvList: [],
      Client: "",
      message: "",
      tempid: [],
      subcribelist: []
    });

    onMounted(() => {
      state.tempid = new Set();
      state.subcribelist = new Set();
    });

    onUnmounted(() => {
      state.subcribelist.forEach(e => {
        const msg = {
          conference_id: e,
          writer: state.userId,
          message: state.userId + "님이 나가셨습니다."
        };
        state.Client.send("/publish/conferences/send", JSON.stringify(msg), {});
      });

      if (state.Client) state.Client.disconnect();
    });

    watch(
      () => state.joinlist,
      () => {
        console.log("state.joinlist", state.joinlist);
        let line = state.joinlist;

        for (let i = 0; i < line.length; i++) {
          console.log("원소", line[i]);
          state.tempid.add(line[i].study_id);
        }
        console.log("확인", state.tempid);

        //const serverURL = "https://i5b306.p.ssafy.io/api/v1/ws";
        const serverURL = "https://localhost:8443/api/v1/ws";
        var socket = new SockJS(serverURL);
        state.stompClient = Stomp.over(socket);

        state.stompClient.connect(
          {},
          frame => {
            // 소켓 연결 성공
            state.connected = true;
            console.log("소켓 연결 성공", frame);
            state.Client = state.stompClient;
            // 서버의 메시지 전송 endpoint를 구독합니다.
            // 이런형태를 pub sub 구조라고 합니다.

            var sendurl = "/publish/conferences/join";
            //var temp = "/subscribe";
            state.tempid.forEach(e => {
              if (!state.subcribelist.has(e)) {
                state.subcribelist.add(e);
                console.log("구독 방번호", e);
                var temp = "/subscribe/" + "conferences/" + e;
                state.stompClient.subscribe(temp, res => {
                  console.log("구독으로 받은 메시지 입니다.", res.body);
                  let temp = JSON.parse(res.body);
                  // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
                  state.recvList.push(temp);
                  if (!state.visible && !(temp.writer === state.userId))
                    state.length += 1;
                });

                const msg = {
                  conference_id: e,
                  writer: state.userId,
                  message: state.message
                };
                state.Client.send(
                  "/publish/conferences/join",
                  JSON.stringify(msg),
                  {}
                );
              }
            });

            // for (let i = 0; i < state.tempid.length; i++) {
            //   var temp = "/subscribe/" + "conferences/" + state.tempid[i];
            //   state.stompClient.subscribe(temp, res => {
            //     console.log("구독으로 받은 메시지 입니다.", res.body);
            //     let temp = JSON.parse(res.body);
            //     // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
            //     state.recvList.push(temp);
            //     if (!state.visible && temp.writer != state.userId)
            //       state.length++;
            //   });
            // }
          },
          error => {
            // 소켓 연결 실패
            console.log("소켓 연결 실패", error);
            state.connected = false;
          }
        );
      }
    );

    watch(
      () => state.value,
      () => {
        router.push({
          name: state.value
          // params: {
          //   studypk: state.studyPk
          // }
        });
      }
    );

    watch(
      () => state.value2,
      () => {
        //state.value = state.value2;
        state.options.forEach(e => {
          if (e.value == state.value2) {
            state.label = e.label;
          }
        });

        router.push({
          name: state.value2
        });
      }
    );

    if (state.activeIndex === -1) {
      state.activeIndex = 0;
      store.commit("root/setMenuActive", 0);
    }

    const menuSelect = function(param) {
      store.commit("root/setMenuActive", param);
      const MenuItems = store.getters["root/getMenus"];
      let keys = Object.keys(MenuItems);
      console.log("여기역이경", keys, param);
      router.push({
        name: keys[param]
      });
    };

    const clickLogo = () => {
      router
        .push({
          name: "main"
        })
        .then(() => {
          localStorage.removeItem("studypk");
          store.commit("root/setMenuActive", 0);
          store.commit("root/setSelectOption", "");
          store.commit("root/setStudypk", 0);
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

      state.subcribelist.forEach(e => {
        const msg = {
          conference_id: e,
          writer: state.userId,
          message: state.userId + "님이 나가셨습니다."
        };
        state.Client.send("/publish/conferences/send", JSON.stringify(msg), {});
      });

      if (state.Client) state.Client.disconnect();
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

    const clickOtherpeoplepage = () => {
      emit("openOtherpeopleDialog");
    };

    const changeCollapse = () => {
      state.isCollapse = !state.isCollapse;
    };
    const gotodetail = file => {
      state.label = file.label;
      state.icon = file.icon;
      router.push({
        name: file.value
        // params: {
        //   studypk: state.studyPk
        // }
      });
    };

    const reversea = function() {
      state.visible = !state.visible;
      if (state.visible) state.length = 0;
    };

    const sendMessagePub = function(e) {
      console.log(state.userId);
      console.log(state.message);
      if (e.keyCode === 13 && state.userId !== "" && state.message !== "") {
        //send();
        console.log("Send message:" + state.message);
        console.log(state.Client);
        console.log(state.connected);
        if (state.Client && state.connected) {
          const msg = {
            conference_id: 1,
            writer: state.userId,
            message: state.message
          };

          console.log(msg);
          state.Client.send(
            "/publish/conferences/send",
            JSON.stringify(msg),
            {}
          );
        }
        state.message = "";
      }
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
      clicktestanswer,
      clickOtherpeoplepage,
      gotodetail,
      reversea,
      sendMessagePub
    };
  }
};
</script>
<style>
.main-header {
  padding: 5px 20px;
  background-color: rgb(143, 209, 141);
}
/*Mobile, Tablet*/
.menu-icon-wrapper {
  display: inline-block;
  vertical-align: center;
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
.logo-ppakgom {
  font-size: 32px;
  font-weight: bold;
  color: #005005;
  cursor: pointer;
}
.main-header .hide-on-small .logo-wrapper {
  cursor: pointer;
  display: inline-block;
}
.main-header .hide-on-small .logo-wrapper .ic.ic-logo {
  width: 70px;
  height: 60px;
  background-size: contain;
  background-repeat: no-repeat;
  background-image: url("../../../assets/images/ppakgom.png");
}
.main-header .hide-on-small .tool-wrapper {
  width: 50%;
  float: right;
}
/* .main-header .hide-on-small .tool-wrapper .button-wrapper {
  width: 45%;
  float: right;
}
.main-header .hide-on-small .tool-wrapper .button-wrapper .el-button {
  width: 45%;
  height: 50px;
  cursor: pointer;
  margin-right: 1%;
} */

.main-header .hide-on-small .tool-wrapper .el-input .el-input__inner {
  /* width: 88%; */
  height: 50px;
  margin-right: 1%;
}
.main-header .hide-on-small .tool-wrapper .el-input .el-input__prefix {
  top: 5px;
}

.otherchatbox {
  border: 1px solid #111111;
  border-radius: 10px;
  padding: 6px;
  margin: 5px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.12), 0 0 8px rgba(0, 0, 0, 0.04);
  word-break: break-all;
  align-content: left;
  text-align: left;
  display: block;
  flex: left;
}
</style>
