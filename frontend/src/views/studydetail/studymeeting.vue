<template>
  <!-- <div>
    <window-popup v-model="open">팝업창의 내용입니다.</window-popup>
    <button @click="open = true">open</button>
    {{ open }}
  </div> -->
  <el-container style="height:97%">
    <el-main>
      <div id="join" v-if="!session">
        <div id="join-dialog" class="jumbotron vertical-center">
          <h1>Join a video session</h1>
          <div class="form-group">
            <p>
              <label>Participant</label>
              <input
                v-model="myUserName"
                class="form-control"
                type="text"
                required
              />
            </p>
            <p>
              <label>Session</label>
              <input
                v-model="mySessionId"
                class="form-control"
                type="text"
                required
              />
            </p>
            <p class="text-center">
              <button class="btn btn-lg btn-success" @click="joinSession()">
                Join!
              </button>
            </p>
          </div>
        </div>
      </div>

      <div id="session" v-if="session">
        <el-row>
          <div id="session-header">
            <h1 id="session-title">{{ mySessionId }}</h1>
          </div>
        </el-row>

        <!-- <div id="main-video" class="col-md-6">
        <user-video :stream-manager="mainStreamManager" />
      </div> -->
        <el-row>
          <el-col :span="18">
            <el-container class="room">
              <user-video
                class="screen-res"
                :stream-manager="publisher"
                @click="updateMainVideoStreamManager(publisher)"
              />
              <user-video
                class="screen-res"
                v-for="sub in subscribers"
                :key="sub.stream.connection.connectionId"
                :stream-manager="sub"
                @click="updateMainVideoStreamManager(sub)"
              />
            </el-container>
          </el-col>
          <el-col :span="6">
            <el-collapse>
              <el-collapse-item title="채팅" name="1">
                <el-scrollbar max-height="400px">
                  <p class="item" v-for="item in count">{{ item }}</p>
                </el-scrollbar>
              </el-collapse-item>
            </el-collapse>
          </el-col>
        </el-row>
      </div>

      <!-- <input v-model="message" type="text" @keyup.enter="sendMessage" /> -->
    </el-main>
  </el-container>
  <el-container style="height:3%">
    <el-footer>
      <el-button-group v-if="session">
        <el-button
          v-if="audioOn"
          type="success"
          @click="audioOnOOff"
          icon="el-icon-microphone"
          >음성중</el-button
        >

        <el-button
          v-if="!audioOn"
          type="danger"
          @click="audioOnOOff"
          icon="el-icon-turn-off-microphone"
          >음소거</el-button
        >

        <el-button
          v-if="videoOn"
          type="success"
          @click="videoOnOOff"
          icon="el-icon-video-pause"
          >화상 시작</el-button
        >

        <el-button
          v-if="!videoOn"
          type="danger"
          @click="videoOnOOff"
          icon="el-icon-video-play"
          >화상 중지</el-button
        >
        <el-button type="success" icon="el-icon-monitor">화상공유</el-button>
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
import WindowPopup from "./WindowPopup";

axios.defaults.headers.post["Content-Type"] = "application/json";

const OPENVIDU_SERVER_URL = "https://" + "i5b306.p.ssafy.io" + ":9000";
const OPENVIDU_SERVER_SECRET = "MY_SECRET";

export default {
  name: "studymeeting",

  components: {
    UserVideo,
    WindowPopup
  },

  data() {
    return {
      count: 3,
      open: false,
      message: "",
      OV: undefined,
      session: undefined,
      mainStreamManager: undefined,
      publisher: undefined,
      subscribers: [],
      audioOn: true,
      videoOn: true,
      mySessionId: "SessionA",
      myUserName: "Participant" + Math.floor(Math.random() * 100)
    };
  },
  mounted: function() {
    console.log("들어가기전 훅");
  },
  beforeDestroy: function() {
    if (this.session) this.session.disconnect();
    window.removeEventListener("beforeunload", this.leaveSession);
    console.log("파괴");
  },
  methods: {
    joinSession() {
      // --- Get an OpenVidu object ---
      this.OV = new OpenVidu();

      // --- Init a session ---
      this.session = this.OV.initSession();

      // --- Specify the actions when events take place in the session ---

      // On every new Stream received...
      this.session.on("streamCreated", ({ stream }) => {
        const subscriber = this.session.subscribe(stream);
        this.subscribers.push(subscriber);
      });

      // On every Stream destroyed...
      this.session.on("streamDestroyed", ({ stream }) => {
        const index = this.subscribers.indexOf(stream.streamManager, 0);
        if (index >= 0) {
          this.subscribers.splice(index, 1);
        }
      });

      // On every asynchronous exception...
      this.session.on("exception", ({ exception }) => {
        console.warn(exception);
      });

      // --- Connect to the session with a valid user token ---

      // 'getToken' method is simulating what your server-side should do.
      // 'token' parameter should be retrieved and returned by your own backend
      this.getToken(this.mySessionId).then(token => {
        this.session
          .connect(token, { clientData: this.myUserName })
          .then(() => {
            // --- Get your own camera stream with the desired properties ---

            let publisher = this.OV.initPublisher(undefined, {
              audioSource: undefined, // The source of audio. If undefined default microphone
              videoSource: undefined, // The source of video. If undefined default webcam
              publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
              publishVideo: true, // Whether you want to start publishing with your video enabled or not
              resolution: "640x480", // The resolution of your video
              frameRate: 30, // The frame rate of your video
              insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
              mirror: false // Whether to mirror your local video or not
            });

            this.mainStreamManager = publisher;
            this.publisher = publisher;

            // --- Publish your stream ---

            this.session.publish(this.publisher);
          })
          .catch(error => {
            console.log(
              "There was an error connecting to the session:",
              error.code,
              error.message
            );
          });
      });
      this.session.on("signal:chat", event => {
        let eventData = JSON.parse(event.data);
        console.log("받은 메시지", eventData);
      });
      window.addEventListener("beforeunload", this.leaveSession);
    },

    leaveSession() {
      // --- Leave the session by calling 'disconnect' method over the Session object ---
      if (this.session) this.session.disconnect();

      this.session = undefined;
      this.mainStreamManager = undefined;
      this.publisher = undefined;
      this.subscribers = [];
      this.OV = undefined;
      this.audioOn = true;
      this.videoOn = true;

      window.removeEventListener("beforeunload", this.leaveSession);
    },

    updateMainVideoStreamManager(stream) {
      if (this.mainStreamManager === stream) return;
      this.mainStreamManager = stream;
    },
    audioOnOOff() {
      this.audioOn = !this.audioOn;

      this.publisher.publishAudio(this.audioOn);
    },

    videoOnOOff() {
      this.videoOn = !this.videoOn;

      this.publisher.publishVideo(this.videoOn);
    },
    /**
     * --------------------------
     * SERVER-SIDE RESPONSIBILITY
     * --------------------------
     * These methods retrieve the mandatory user token from OpenVidu Server.
     * This behavior MUST BE IN YOUR SERVER-SIDE IN PRODUCTION (by using
     * the API REST, openvidu-java-client or openvidu-node-client):
     *   1) Initialize a Session in OpenVidu Server	(POST /openvidu/api/sessions)
     *   2) Create a Connection in OpenVidu Server (POST /openvidu/api/sessions/<SESSION_ID>/connection)
     *   3) The Connection.token must be consumed in Session.connect() method
     */

    getToken(mySessionId) {
      return this.createSession(mySessionId).then(sessionId =>
        this.createToken(sessionId)
      );
    },

    // See https://docs.openvidu.io/en/stable/reference-docs/REST-API/#post-openviduapisessions
    createSession(sessionId) {
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
    },
    sendMessage() {
      var messageData = {
        content: this.message,
        secretName: this.myUserName
      };
      this.session.signal({
        type: "chat",
        data: JSON.stringify(messageData),
        to: []
      });
    },
    // See https://docs.openvidu.io/en/stable/reference-docs/REST-API/#post-openviduapisessionsltsession_idgtconnection
    createToken(sessionId) {
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
    }
  }
};
</script>
<style>
.screen-res {
  position: block;
  height: auto;
  width: inherit;
  /* min-width: 33%; */
  max-width: 33%;
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

#main-video p {
  position: absolute;
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
