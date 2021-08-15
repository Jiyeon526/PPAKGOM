<template>
  <!-- <div>
    <window-popup v-model="open">팝업창의 내용입니다.</window-popup>
    <button @click="open = true">open</button>
    {{ open }}
  </div> -->
  <el-container style="height:97%; flex-wrap: wrap;">
    <el-main>
      <div id="join" v-if="!state.session">
        <div id="join-dialog" class="jumbotron vertical-center">
          <h1>입장 전 거울 화면</h1>
          <video id="test-video" autoplay loop muted></video>
          <div class="form-group">
            <!-- <p>
              <label>Participant</label>
              <input
                v-model="state.myUserName"
                class="form-control"
                type="text"
                required
              />
            </p>
            <p>
              <label>Session</label>
              <input
                v-model="state.mySessionId"
                class="form-control"
                type="text"
                required
              />
            </p> -->

            <el-button @click="joinSession()" type="success" round
              >화상 미팅 입장</el-button
            >
          </div>
        </div>
      </div>

      <div id="session" v-if="state.session">
        <!-- <el-row>
          <div id="session-header">
            <h1 id="session-title">{{ mySessionId }}</h1>
          </div>
        </el-row> -->

        <el-row :gutter="20">
          <el-col :span="state.wide">
            <el-container class="room">
              <user-video
                class="main-stream"
                v-if="state.mainmode"
                :stream-manager="state.mainStreamManager"
              />
            </el-container>
            <el-container class="room" v-if="!state.mainmode">
              <user-video
                class="screen-res"
                :stream-manager="state.publisher"
                @click="updateMainVideoStreamManager(state.publisher)"
              />
              <user-video
                class="screen-res"
                v-for="sub in state.subscribers"
                :key="sub.stream.connection.connectionId"
                :stream-manager="sub"
                @click="updateMainVideoStreamManager(sub)"
              />
              <!-- <user-video
                class="screen-res"
                :stream-manager="state.screenPublisher"
                @click="updateMainVideoStreamManager(state.screenPublisher)"
              />
              <user-video
                class="screen-res"
                v-for="sub in state.screenSubscribers"
                :key="sub.stream.connection.connectionId"
                :stream-manager="sub"
                @click="updateMainVideoStreamManager(sub)"
              /> -->
            </el-container>
            <el-container class="room" v-if="state.mainmode">
              <user-video
                class="screen-res-small"
                :stream-manager="state.publisher"
                @click="updateMainVideoStreamManager(state.publisher)"
              />
              <user-video
                class="screen-res-small"
                v-for="sub in state.subscribers"
                :key="sub.stream.connection.connectionId"
                :stream-manager="sub"
                @click="updateMainVideoStreamManager(sub)"
              />
              <!-- <user-video
                class="screen-res"
                :stream-manager="state.screenPublisher"
                @click="updateMainVideoStreamManager(state.screenPublisher)"
              />
              <user-video
                class="screen-res"
                v-for="sub in state.screenSubscribers"
                :key="sub.stream.connection.connectionId"
                :stream-manager="sub"
                @click="updateMainVideoStreamManager(sub)"
              /> -->
            </el-container>
          </el-col>
          <el-col :span="state.narrow">
            <el-popover placement="bottom-start" :width="580" trigger="click">
              <template #reference>
                <el-button @click="widthreverse">채팅</el-button>
              </template>
              <div style="height:550px;">
                <el-collapse v-model="state.activeNames">
                  <el-collapse-item title="채팅" name="1">
                    <!-- <el-scrollbar height="500px" id="chat-area"> -->

                    <div id="chat-area" style=" height:520px; overflow:scroll;">
                      <div v-for="(item, i) in state.messages" :key="i">
                        <el-avatar
                          src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                        ></el-avatar>
                        {{ item.from }}:{{ item.content }}
                      </div>
                    </div>

                    <!-- </el-scrollbar> -->
                  </el-collapse-item>
                </el-collapse>
              </div>
              <el-input
                v-model="state.message"
                type="textarea"
                clearable
                :rows="2"
                placeholder="채팅 내용을 입력하세요"
                maxlength="100"
                @keyup.enter="sendMessage"
                show-word-limit
              />
            </el-popover>
          </el-col>
        </el-row>
      </div>
    </el-main>
  </el-container>
  <el-container>
    <el-footer>
      <el-button-group v-if="state.session">
        <el-button
          v-if="state.audioOn"
          type="success"
          @click="audioOnOOff"
          icon="el-icon-microphone"
          >음성중</el-button
        >

        <el-button
          v-if="!state.audioOn"
          type="danger"
          @click="audioOnOOff"
          icon="el-icon-turn-off-microphone"
          >음소거</el-button
        >

        <el-button
          v-if="state.videoOn"
          type="success"
          @click="videoOnOOff"
          icon="el-icon-video-pause"
          >화상 시작</el-button
        >

        <el-button
          v-if="!state.videoOn"
          type="danger"
          @click="videoOnOOff"
          icon="el-icon-video-play"
          >화상 중지</el-button
        >

        <el-button
          type="success"
          icon="el-icon-monitor"
          v-if="!state.screenSession"
          @click="toggleShareScreen"
          >화상공유</el-button
        >

        <el-button
          type="danger"
          icon="el-icon-s-platform"
          v-if="state.screenSession"
          @click="toggleShareScreen"
          >공유중지</el-button
        >

        <el-button
          type="success"
          icon="el-icon-full-screen"
          v-if="!state.mainmode"
          @click="toggleMainmode"
          >메인모드</el-button
        >

        <el-button
          type="danger"
          icon="el-icon-crop"
          v-if="state.mainmode"
          @click="toggleMainmode"
          >분할모드</el-button
        >
        <el-button
          type="success"
          icon="el-icon-switch-button"
          @click="leaveSession"
          >나가기</el-button
        >
      </el-button-group>
    </el-footer>
  </el-container>
</template>

<script>
import axios from "axios";
import { OpenVidu } from "openvidu-browser";
import UserVideo from "./openviducomponents/UserVideo";
import Swal from "sweetalert2";
import { ElNotification, ElMessageBox, ElMessage } from "element-plus";
import { onMounted, onUnmounted, reactive } from "vue";
import { useStore } from "vuex";
import { useRoute } from "vue-router";

axios.defaults.headers.post["Content-Type"] = "application/json";

const OPENVIDU_SERVER_URL = "https://" + "i5b306.p.ssafy.io" + ":9000";
const OPENVIDU_SERVER_SECRET = "GOM_SECRET";

export default {
  name: "studymeeting",

  components: {
    UserVideo
  },
  setup() {
    const store = useStore();
    const router = useRoute();

    const state = reactive({
      userpk: 0,

      //메인모드
      mainmode: false,
      // 화면 크기 변화
      widthflag: false,
      wide: 22,
      narrow: 2,

      //채팅
      message: "",
      messages: [],
      activeNames: ["1"],

      //화상회의
      OV: undefined,
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],
      mySessionId: "SessionA",
      //myUserName: "Participant" + Math.floor(Math.random() * 100),
      myUserName: "default" + Math.floor(Math.random() * 100),
      //마이크 ONOFF 카메라 ONOFF
      audioOn: true,
      videoOn: true,

      //화면공유
      screenOV: undefined,
      screenSession: undefined,
      screenMainStreamManager: undefined,
      screenPublisher: undefined,
      screenSubscribers: [],
      screenOvToken: null,
      isSharingMode: false,

      //확인 스피크
      isSpeackList: [],

      //입장 전 화면
      stream: ""
    });
    // 페이지 진입시 불리는 훅
    onMounted(() => {
      state.userpk = store.getters["root/getStudypk"];
      console.log("방번호", state.userpk);
      state.mySessionId = "room" + state.userpk;
      console.log("방이름", state.mySessionId);
      store
        .dispatch("root/requestReadMyInfo")
        .then(function(result) {
          state.myUserName = result.data.name + Math.floor(Math.random() * 100);

          navigator.getUserMedia =
            navigator.getUserMedia ||
            navigator.webkitGetUserMedia ||
            navigator.mozGetUserMedia ||
            navigator.msGetUserMedia;
          var video = document.getElementById("test-video");
          navigator.getUserMedia(
            { video: true, audio: false },
            function(stream) {
              state.stream = stream;
              video.srcObject = stream;
              video.play();
            },
            function(error) {}
          );
        })
        .catch(function(err) {
          ElMessage.error(err);
          state.myUserName = "error" + Math.floor(Math.random() * 100);
        });
    });
    onUnmounted(() => {
      if (state.screenSubscribers) stopShareScreen();
      if (state.session) leaveSession();
      window.removeEventListener("beforeunload", leaveSession);
    });

    const toggleMainmode = function() {
      state.mainmode = !state.mainmode;
    };

    const widthreverse = function() {
      if (state.widthflag) {
        state.wide = 22;
        state.narrow = 2;
        state.widthflag = !state.widthflag;
      } else {
        state.wide = 18;
        state.narrow = 6;
        state.widthflag = !state.widthflag;
      }
    };

    const joinSession = function() {
      // --- Get an OpenVidu object ---
      state.stream.getTracks().forEach(track => track.stop());
      state.OV = new OpenVidu();

      state.OV.setAdvancedConfiguration({
        publisherSpeakingEventsOptions: {
          interval: 100, // Frequency of the polling of audio streams in ms (default 100)
          threshold: -50 // Threshold volume in dB (default -50)
        }
      });

      state.session = state.OV.initSession();

      // --- Init a session ---

      // --- Specify the actions when events take place in the session ---

      // On every new Stream received...
      state.session.on("publisherStartSpeaking", event => {
        // console.log(
        //   "User " + event.connection.connectionId + " start speaking"
        // );
        state.isSpeackList.push(event.connection.connectionId);
      });

      state.session.on("publisherStopSpeaking", event => {
        // console.log("User " + event.connection.connectionId + " stop speaking");
        let temp = state.isSpeackList;
        let index = temp.indexOf(event.connection.connectionId, 0);
        if (index >= 0) {
          temp.splice(index, 1);
          state.isSpeackList = temp;
        }
      });

      state.session.on("streamCreated", ({ stream }) => {
        const subscriber = state.session.subscribe(stream);
        let client = JSON.parse(subscriber.stream.connection.data);

        ElNotification({
          title: "입장",
          message: `${client.clientData} 님이 입장하셨습니다.`,
          type: "success"
        });
        state.subscribers.push(subscriber);
      });

      // On every Stream destroyed...
      state.session.on("streamDestroyed", ({ stream }) => {
        const index = state.subscribers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          let client = JSON.parse(
            state.subscribers[index].stream.connection.data
          );

          ElNotification({
            title: "퇴장",
            message: `${client.clientData} 님이 퇴장하셨습니다.`,
            type: "warning"
          });
          state.subscribers.splice(index, 1);
        }
      });
      state.session.on("signal:share", event => {
        if (event.data === "F") {
          state.isSharingMode = false;
        } else {
          state.isSharingMode = true;
        }
      });
      // On every asynchronous exception...
      state.session.on("exception", ({ exception }) => {
        console.warn(exception);
      });

      // --- Connect to the session with a valid user token ---

      // 'getToken' method is simulating what your server-side should do.
      // 'token' parameter should be retrieved and returned by your own backend
      getToken(state.mySessionId).then(token => {
        state.session
          .connect(token, { clientData: state.myUserName })
          .then(() => {
            // --- Get your own camera stream with the desired properties ---

            let publisher = state.OV.initPublisher(undefined, {
              audioSource: undefined, // The source of audio. If undefined default microphone
              videoSource: undefined, // The source of video. If undefined default webcam
              publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
              publishVideo: true, // Whether you want to start publishing with your video enabled or not
              resolution: "960x540", // The resolution of your video
              frameRate: 30, // The frame rate of your video
              insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
              mirror: false // Whether to mirror your local video or not
            });

            state.mainStreamManager = publisher;
            state.publisher = publisher;

            // --- Publish your stream ---

            state.session.publish(state.publisher);
          })
          .catch(error => {
            console.log(
              "There was an error connecting to the session:",
              error.code,
              error.message
            );
          });
      });
      var num = 0;
      state.session.on("signal:chat", event => {
        let eventData = JSON.parse(event.data);
        state.messages.push(eventData);
        setTimeout(() => {
          var chatDiv = document.getElementById("chat-area");

          console.log(chatDiv);
          console.log(chatDiv.scrollHeight);
          console.log(chatDiv.clientHeight);
          console.log(num);
          chatDiv.scrollTo({
            top: chatDiv.scrollHeight,
            behavior: "smooth"
          });
          num += 20;
        }, 50);
      });
      window.addEventListener("beforeunload", leaveSession);
    };

    const leaveSession = function() {
      // --- Leave the session by calling 'disconnect' method over the Session object ---
      if (state.session) state.session.disconnect();

      state.session = undefined;
      state.mainStreamManager = undefined;
      state.publisher = undefined;
      state.subscribers = [];
      state.OV = undefined;
      state.audioOn = true;
      state.videoOn = true;
      state.messages = [];
      window.removeEventListener("beforeunload", leaveSession);
      setTimeout(() => {
        navigator.getUserMedia =
          navigator.getUserMedia ||
          navigator.webkitGetUserMedia ||
          navigator.mozGetUserMedia ||
          navigator.msGetUserMedia;
        var video = document.getElementById("test-video");
        navigator.getUserMedia(
          { video: true, audio: false },
          function(stream) {
            video.srcObject = stream;
            video.play();
          },
          function(error) {}
        );
      }, 500);
      // store.commit("root/setSelectOption", "studyhome");
      // router.push({ name: "studyhome" });
    };

    const updateMainVideoStreamManager = function(stream) {
      if (state.mainStreamManager === stream) return;
      console.log(stream);
      state.mainStreamManager = stream;
    };
    const audioOnOOff = function() {
      state.audioOn = !state.audioOn;

      state.publisher.publishAudio(state.audioOn);
    };

    const videoOnOOff = function() {
      state.videoOn = !state.videoOn;

      state.publisher.publishVideo(state.videoOn);
    };

    const getToken = function(mySessionId) {
      return createSession(mySessionId).then(sessionId =>
        createToken(sessionId)
      );
    };

    // See https://docs.openvidu.io/en/stable/reference-docs/REST-API/#post-openviduapisessions
    const createSession = function(sessionId) {
      return new Promise((resolve, reject) => {
        axios
          .post(
            `${OPENVIDU_SERVER_URL}/openvidu/api/sessions`,
            JSON.stringify({
              customSessionId: sessionId
            }),
            {
              auth: {
                username: "OPENVIDUAPP",
                password: OPENVIDU_SERVER_SECRET
              }
            }
          )
          .then(response => response.data)
          .then(data => resolve(data.id))
          .catch(error => {
            if (error.response.status === 409) {
              resolve(sessionId);
            } else {
              console.warn(
                `No connection to OpenVidu Server. This may be a certificate error at ${OPENVIDU_SERVER_URL}`
              );
              if (
                window.confirm(
                  `No connection to OpenVidu Server. This may be a certificate error at ${OPENVIDU_SERVER_URL}\n\nClick OK to navigate and accept it. If no certificate warning is shown, then check that your OpenVidu Server is up and running at "${OPENVIDU_SERVER_URL}"`
                )
              ) {
                location.assign(`${OPENVIDU_SERVER_URL}/accept-certificate`);
              }
              reject(error.response);
            }
          });
      });
    };
    const sendMessage = function() {
      var messageData = {
        content: state.message,
        from: state.myUserName
      };
      state.message = "";
      state.session.signal({
        type: "chat",
        data: JSON.stringify(messageData),
        to: []
      });
    };
    // See https://docs.openvidu.io/en/stable/reference-docs/REST-API/#post-openviduapisessionsltsession_idgtconnection
    const createToken = function(sessionId) {
      return new Promise((resolve, reject) => {
        axios
          .post(
            `${OPENVIDU_SERVER_URL}/openvidu/api/sessions/${sessionId}/connection`,
            {},
            {
              auth: {
                username: "OPENVIDUAPP",
                password: OPENVIDU_SERVER_SECRET
              }
            }
          )
          .then(response => response.data)
          .then(data => resolve(data.token))
          .catch(error => reject(error.response));
      });
    };
    const toggleShareScreen = function() {
      if (state.screenPublisher) {
        ElMessageBox.confirm("화면 공유를 중지하겠습니까?", "Warning", {
          confirmButtonText: "OK",
          cancelButtonText: "Cancel",
          type: "warning",
          center: true
        })
          .then(() => {
            stopShareScreen();
            ElMessage({
              message: "공유를 중지합니다",
              type: "success"
            });
          })
          .catch(() => {
            ElMessage({
              type: "info",
              message: "공유~!"
            });
          });
      } else {
        ElMessageBox.confirm("화면 공유를 시작하겠습니까?", "Warning", {
          confirmButtonText: "OK",
          cancelButtonText: "Cancel",
          type: "warning",
          center: true
        })
          .then(() => {
            startShareScreen();
            ElMessage({
              message: "공유를 시작합니다",
              type: "success"
            });
          })
          .catch(() => {
            ElMessage({
              type: "info",
              message: "응 공유 안해"
            });
          });
      }
    };

    const stopShareScreen = function() {
      if (state.screenSession) state.screenSession.disconnect();
      state.screenOV = undefined;
      state.screenMainStreamManager = undefined;
      state.screenPublisher = undefined;
      state.screenSession = undefined;
      state.screenSubscribers = [];
      state.screenOvToken = null;
      // state.session.signal({
      //   data: "F",
      //   to: [],
      //   type: "share"
      // });
    };
    const startShareScreen = function() {
      const screenOV = new OpenVidu();

      const screenSession = screenOV.initSession();

      getToken(state.mySessionId).then(token2 => {
        let screenPublisher = screenOV.initPublisher(undefined, {
          audioSource: false, // The source of audio. If undefined default microphone
          videoSource: "screen", // The source of video. If undefined default webcam
          publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
          publishVideo: true, // Whether you want to start publishing with your video enabled or not
          resolution: "1440x1080", // The resolution of your video
          frameRate: 30, // The frame rate of your video
          insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
          mirror: false // Whether to mirror your local video or not
        });
        screenSession
          .connect(token2, { clientData: state.myUserName + " 화면" })
          .then(() => {
            screenPublisher.once("accessAllowed", () => {
              screenPublisher.stream
                .getMediaStream()
                .getVideoTracks()[0]
                .addEventListener("ended", () => {
                  dispatch("stopShareScreen");
                });
              screenSession.publish(screenPublisher);
              state.screenOV = screenOV;
              state.screenMainStreamManager = screenPublisher;
              state.screenPublisher = screenPublisher;
              state.screenSession = screenSession;
              state.screenSubscribers = screenSubscribers;
              state.screenOvToken = token2;
              state.session.signal({
                data: "T",
                to: [],
                type: "share"
              });
            });
            screenPublisher.once("accessDenied", () => {
              console.warn("ScreenShare: Access Denied");
            });
            screenPublisher.once("accessDenied", () => {
              console.warn("ScreenShare: Access Denied");
            });
          })
          .catch(error => {
            console.log(
              "There was an error connecting to the session:",
              error.code,
              error.message
            );
          });
      });
    };
    return {
      state,
      toggleMainmode,
      widthreverse,
      joinSession,
      leaveSession,
      updateMainVideoStreamManager,
      audioOnOOff,
      videoOnOOff,
      getToken,
      createSession,
      sendMessage,
      createToken,
      toggleShareScreen,
      startShareScreen,
      stopShareScreen
    };
  }
  // beforeDestroy: function() {
  //   //if (this.session) this.session.disconnect();
  //   leaveSession();
  //   window.removeEventListener("beforeunload", this.leaveSession);
  //   console.log("파괴");
  // },
};
</script>
<style>
#test-video {
  max-width: 60%;
}

.main-stream {
  max-width: 70%;
}
.message-wrap {
  padding: 0 15px;
  height: calc(100% - 80px);
  overflow: auto;
}

.screen-res {
  position: block;
  height: auto;
  width: inherit;
  /* min-width: 33%; */
  max-width: 33%;
}

.screen-res-small {
  position: block;
  height: auto;
  width: inherit;
  /* min-width: 33%; */
  max-width: 16%;
}
.room {
  flex-wrap: wrap;
  align-items: center;
  position: relative;
  max-width: 100%;
  max-height: 100%;
  overflow: hidden;
}

.room p {
  padding-left: 1px;
  padding-right: 1px;
  color: #ffffff;
  font-weight: bold;
  border-bottom-right-radius: 1px;
}

.room video + div {
  float: left;
  position: relative;
  margin-left: -50%;
}

.room video {
  float: left;
  display: block;
  cursor: pointer;

  height: auto;
}
/*vertical-center {
	position: relative;
	top: 30%;
	left: 50%;
	transform: translate(-50%, -50%);
}*/

.footer {
  position: relative;
  bottom: 20;

  height: 0px;
  background-color: #4d4d4d;
}

.footer .text-muted {
  margin: 20px 0;
  align-items: center;
  color: #ccc;
}

.openvidu-logo {
  height: 35px;
  float: right;
  margin: 12px 0;
  -webkit-transition: all 0.1s ease-in-out;
  -moz-transition: all 0.1s ease-in-out;
  -o-transition: all 0.1s ease-in-out;
  transition: all 0.1s ease-in-out;
}

.openvidu-logo:hover {
  -webkit-filter: grayscale(0.5);
  filter: grayscale(0.5);
}

.demo-logo {
  margin: 0;
  height: 22px;
  float: left;
  padding-right: 8px;
}

a:hover .demo-logo {
  -webkit-filter: brightness(0.7);
  filter: brightness(0.7);
}

#join-dialog {
  margin-left: auto;
  margin-right: auto;
  max-width: 70%;
}

#join-dialog h1 {
  color: #4d4d4d;
  font-weight: bold;
  text-align: center;
}

#img-div {
  text-align: center;
  margin-top: 3em;
  margin-bottom: 3em;
  /*position: relative;
	top: 20%;
	left: 50%;
	transform: translate(-50%, -50%);*/
}

#img-div img {
  height: 15%;
}

#join-dialog label {
  color: #0088aa;
}

#join-dialog input.btn {
  margin-top: 15px;
}

#session-header {
  margin-bottom: 20px;
}

#session-title {
  display: inline-block;
}

#buttonLeaveSession {
  float: right;
  margin-top: 20px;
}

#video-container video {
  position: relative;
  float: left;
  width: 50%;
  cursor: pointer;
}

#video-container video + div {
  float: left;
  width: 50%;
  position: relative;
  margin-left: -50%;
}

#video-container p {
  display: inline-block;
  background: #f8f8f8;
  padding-left: 5px;
  padding-right: 5px;
  color: #777777;
  font-weight: bold;
  border-bottom-right-radius: 4px;
}

video {
  width: 100%;
  height: auto;
}
#main-video {
  position: relative;
  display: inline-block;
  background: #f8f8f8;
  padding-left: 2px;
  padding-right: 2px;
  font-size: 22px;
  color: #777777;
  font-weight: bold;
  border-bottom-right-radius: 4px;
}

#main-video p {
  position: relative;
  display: inline-block;
  background: #f8f8f8;
  padding-left: 5px;
  padding-right: 5px;
  font-size: 22px;
  color: #777777;
  font-weight: bold;
  border-bottom-right-radius: 4px;
}

#session img {
  width: 100%;
  height: auto;
  display: inline-block;
  object-fit: contain;
  vertical-align: baseline;
}

#session #video-container img {
  position: relative;
  float: left;
  width: 50%;
  cursor: pointer;
  object-fit: cover;
  height: 180px;
}

/* xs ans md screen resolutions*/

@media screen and (max-width: 991px) and (orientation: portrait) {
  #join-dialog {
    max-width: inherit;
  }
  #img-div img {
    height: 10%;
  }
  #img-div {
    margin-top: 2em;
    margin-bottom: 2em;
  }
  .container-fluid > .navbar-collapse,
  .container-fluid > .navbar-header,
  .container > .navbar-collapse,
  .container > .navbar-header {
    margin-right: 0;
    margin-left: 0;
  }
  .navbar-header i.fa {
    font-size: 30px;
  }
  .navbar-header a.nav-icon {
    padding: 7px 3px 7px 3px;
  }
}

@media only screen and (max-height: 767px) and (orientation: landscape) {
  #img-div {
    margin-top: 1em;
    margin-bottom: 1em;
  }
  #join-dialog {
    max-width: inherit;
  }
}
</style>
