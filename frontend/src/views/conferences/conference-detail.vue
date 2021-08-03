<template>
  <el-row>
    <el-col :span="18">
      <div class="box">
        <el-image
          style="width: 70px; height: 70px"
          :src="state.thumbnail"
          :fit="fit"
        ></el-image>
        {{ state.title }}
      </div>
    </el-col>
    <el-col :span="6"></el-col>
  </el-row>
  <el-row>
    <el-col :span="18"
      ><div id="container">
        <div id="wrapper">
          <div id="join" class="animate join">
            <button @click="register">시작</button>
          </div>
          <div id="room" style="display: none;">
            <h2 id="room-header"></h2>
            <div id="participants"></div>
          </div>
        </div>
      </div>
    </el-col>
    <el-col :span="6">
      <div>
        내용:
        <input v-model="state.message" type="text" @keyup="sendMessagePub" />
        <div v-for="(item, idx) in state.recvList" :key="idx">
          <h3>{{ item.writer }} : {{ item.message }}</h3>
        </div>
      </div></el-col
    >
  </el-row>
</template>
<style>
#room {
  width: 100%;
  text-align: center;
}

.box {
  display: inline-block;
}
.container {
  margin: 50px auto;
  width: 640px;
}

.participant {
  border-radius: 4px;
  /* border: 2px groove; */
  margin-left: 5;
  margin-right: 5;
  width: 150;
  text-align: center;
  overflow: hide;
  float: left;
  padding: 5px;
  border-radius: 10px;
  -webkit-box-shadow: 0 0 200px rgba(255, 255, 255), 0 1px 2px rgba(0, 0, 0);
  box-shadow: 0 0 200px rgba(255, 255, 255), 0 1px 2px rgba(0, 0, 0);
  /*Transition*/
  -webkit-transition: all 0.3s linear;
  -moz-transition: all 0.3s linear;
  -o-transition: all 0.3s linear;
  transition: all 0.3s linear;
}

.participant:before {
  content: "";
  position: absolute;
  top: -8px;
  right: -8px;
  bottom: -8px;
  left: -8px;
  z-index: -1;
  background: rgb(255, 255, 255);
  border-radius: 4px;
}

.participant:hover {
  opacity: 1;
  background-color: 0a33b6;
  -webkit-transition: all 0.5s linear;
  transition: all 0.5s linear;
}

.participant video,
.participant.main video {
  width: 100%;
  height: auto;
}

.participant span {
  color: rgb(0, 0, 0);
}

.participant.main {
  width: 20%;
  margin: 0 auto;
}

.participant.main video {
  height: auto;
}

.animate {
  -webkit-animation-duration: 0.5s;
  -webkit-animation-fill-mode: both;
  -moz-animation-duration: 0.5s;
  -moz-animation-fill-mode: both;
  -o-animation-duration: 0.5s;
  -o-animation-fill-mode: both;
  -ms-animation-duration: 0.5s;
  -ms-animation-fill-mode: both;
  animation-duration: 0.5s;
  animation-fill-mode: both;
}
</style>
<script>
import {
  reactive,
  onMounted,
  onUnmounted,
  computed,
  onUpdated,
  watch
} from "vue";
import { useStore } from "vuex";
import { useRoute } from "vue-router";
import Stomp from "webstomp-client";
import SockJS from "sockjs-client";
import kurentoUtils from "kurento-utils";
export default {
  name: "conference-detail",

  setup() {
    const route = useRoute();
    const store = useStore();

    const state = reactive({
      token: computed(() => store.getters["root/getAccessToken"]),
      userId: computed(() => store.getters["root/userId"]),
      title: "",
      thumbnail: "",
      conferenceId: "",
      userName: "",
      message: "",
      recvList: [],
      connected: false,
      Client: "",
      ws: null,
      name: "jin",
      room: "123",
      participants: {},
      Participant: ""
    });
    watch(
      () => state.Client,
      Client => {
        const msg = {
          conference_id: state.conferenceId,
          writer: state.userId,
          message: state.message
        };
        state.Client.send("/publish/conferences/join", JSON.stringify(msg), {});
      }
    );
    // 페이지 진입시 불리는 훅
    onMounted(() => {
      state.conferenceId = route.params.conferenceId;
      store.commit("root/setMenuActiveMenuName", "home");
      connect();
      RoomInfoDetail();

      console.log(state.thumbnail);
      store.commit("root/setMenuActiveMenuName", "testvideo");
      state.ws = new WebSocket("wss://" + "localhost:8443" + "/groupcall");

      window.onbeforeunload = function() {
        state.ws.close();
      };
      state.ws.onmessage = function(message) {
        var parsedMessage = JSON.parse(message.data);
        console.info("Received message: " + message.data);
        console.log("onmessage", parsedMessage.id);
        switch (parsedMessage.id) {
          case "existingParticipants":
            onExistingParticipants(parsedMessage);
            break;
          case "newParticipantArrived":
            onNewParticipant(parsedMessage);
            break;
          case "participantLeft":
            onParticipantLeft(parsedMessage);
            break;
          case "receiveVideoAnswer":
            receiveVideoResponse(parsedMessage);
            break;
          case "iceCandidate":
            state.participants[parsedMessage.name].rtcPeer.addIceCandidate(
              parsedMessage.candidate,
              function(error) {
                if (error) {
                  console.error("Error adding candidate: " + error);
                  return;
                }
              }
            );
            break;
          default:
            console.error("Unrecognized message", parsedMessage);
        }
      };
      console.log(state.ws, "마운트 끝");
    });
    onUpdated(() => {});
    // 페이지 이탈시 불리는 훅
    onUnmounted(() => {
      state.conferenceId = "";
      sendMessage({
        id: "leaveRoom"
      });
      console.log("participants", state.participants[0]);
      for (var key in state.participants) {
        state.participants[key].dispose();
      }

      state.ws.close();
      state.Client.disconnect();
    });

    const RoomInfoDetail = () => {
      console.log(state.conferenceId);
      store
        .dispatch("root/requestRoomInfoDetail", state.conferenceId)
        .then(function(res) {
          console.log(res.data);
          var url = "https://localhost:8443/" + res.data.thumbnail_url;
          console.log("url", url);
          state.thumbnail = url;
          state.title = res.data.title;
        });
    };

    const sendMessagePub = function(e) {
      if (e.keyCode === 13 && state.userId !== "" && state.message !== "") {
        //send();
        console.log("Send message:" + state.message);
        console.log(state.Client);
        console.log(state.connected);
        if (state.Client && state.connected) {
          const msg = {
            conference_id: state.conferenceId,
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
    const send = function() {};

    const connect = () => {
      //https://localhost:8443/api/v1/ws
      const serverURL = "https://localhost:8443/api/v1/ws";
      var socket = new SockJS(serverURL);
      var stompClient = Stomp.over(socket);
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`);
      stompClient.connect(
        {},
        frame => {
          // 소켓 연결 성공
          state.connected = true;
          console.log("소켓 연결 성공", frame);
          state.Client = stompClient;
          // 서버의 메시지 전송 endpoint를 구독합니다.
          // 이런형태를 pub sub 구조라고 합니다.
          var temp = "/subscribe/" + "conferences/" + state.conferenceId;
          var sendurl = "/publish/conferences/join";
          //var temp = "/subscribe";
          console.log(temp);
          console.log(stompClient);

          stompClient.subscribe(temp, res => {
            console.log("구독으로 받은 메시지 입니다.", res.body);
            // 받은 데이터를 json으로 파싱하고 리스트에 넣어줍니다.
            state.recvList.push(JSON.parse(res.body));
          });
        },
        error => {
          // 소켓 연결 실패
          console.log("소켓 연결 실패", error);
          state.connected = false;
        }
      );
    };
    function register() {
      state.name = state.userId;
      var room = state.conferenceId;

      //document.getElementById("room-header").innerText = "ROOM " + room;
      document.getElementById("join").style.display = "none";
      document.getElementById("room").style.display = "block";

      var message = {
        id: "joinRoom",
        name: state.name,
        room: room
      };
      console.log(message);
      sendMessage(message);
    }

    function onNewParticipant(request) {
      receiveVideo(request.name);
    }

    function receiveVideoResponse(result) {
      state.participants[result.name].rtcPeer.processAnswer(
        result.sdpAnswer,
        function(error) {
          if (error) return console.error(error);
        }
      );
    }

    function callResponse(message) {
      if (message.response != "accepted") {
        console.info("Call not accepted by peer. Closing call");
        stop();
      } else {
        webRtcPeer.processAnswer(message.sdpAnswer, function(error) {
          if (error) return console.error(error);
        });
      }
    }

    function onExistingParticipants(msg) {
      var constraints = {
        audio: true,
        video: {
          mandatory: {
            maxWidth: 320,
            maxFrameRate: 15,
            minFrameRate: 15
          }
        }
      };
      console.log(state.name + " registered in room " + room);
      var participant = new Participant(state.name);
      state.participants[state.name] = participant;
      var video = participant.getVideoElement();

      var options = {
        localVideo: video,
        mediaConstraints: constraints,
        onicecandidate: participant.onIceCandidate.bind(participant)
      };
      participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendonly(
        options,
        function(error) {
          if (error) {
            return console.error(error);
          }
          this.generateOffer(participant.offerToReceiveVideo.bind(participant));
        }
      );
      console.log(msg.data);
      msg.data.forEach(receiveVideo);
    }
    function receiveVideo(sender) {
      var participant = new Participant(sender);
      state.participants[sender] = participant;
      var video = participant.getVideoElement();

      var options = {
        remoteVideo: video,
        onicecandidate: participant.onIceCandidate.bind(participant)
      };

      participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly(
        options,
        function(error) {
          if (error) {
            return console.error(error);
          }
          this.generateOffer(participant.offerToReceiveVideo.bind(participant));
        }
      );

      console.log("participant.rtcPeer", participant.rtcPeer);
    }

    function onParticipantLeft(request) {
      console.log("Participant " + request.name + " left");
      var participant = state.participants[request.name];
      participant.dispose();
      delete state.participants[request.name];
    }

    function sendMessage(message) {
      var jsonMessage = JSON.stringify(message);
      console.log("Sending message: " + jsonMessage);
      state.ws.send(jsonMessage);
    }

    function Participant(name) {
      const PARTICIPANT_MAIN_CLASS = "participant main";
      const PARTICIPANT_CLASS = "participant";
      //state.name = name;
      var container = document.createElement("div");
      container.className = isPresentMainParticipant()
        ? PARTICIPANT_CLASS
        : PARTICIPANT_MAIN_CLASS;
      container.id = name;
      var span = document.createElement("span");
      var video = document.createElement("video");
      var rtcPeer;

      container.appendChild(video);
      container.appendChild(span);
      container.onclick = switchContainerClass;
      document.getElementById("participants").appendChild(container);

      span.appendChild(document.createTextNode(name));

      video.id = "video-" + name;
      video.autoplay = true;
      video.controls = false;

      this.getElement = function() {
        return container;
      };

      this.getVideoElement = function() {
        return video;
      };

      function switchContainerClass() {
        if (container.className === PARTICIPANT_CLASS) {
          var elements = Array.prototype.slice.call(
            document.getElementsByClassName(PARTICIPANT_MAIN_CLASS)
          );
          elements.forEach(function(item) {
            item.className = PARTICIPANT_CLASS;
          });

          container.className = PARTICIPANT_MAIN_CLASS;
        } else {
          container.className = PARTICIPANT_CLASS;
        }
      }

      function isPresentMainParticipant() {
        return (
          document.getElementsByClassName(PARTICIPANT_MAIN_CLASS).length != 0
        );
      }

      this.offerToReceiveVideo = function(error, offerSdp, wp) {
        if (error) return console.error("sdp offer error");
        console.log("Invoking SDP offer callback function");
        var msg = { id: "receiveVideoFrom", sender: name, sdpOffer: offerSdp };
        sendMessage(msg);
      };

      function sendMessage(message) {
        var jsonMessage = JSON.stringify(message);
        console.log("Sending message: " + jsonMessage);
        state.ws.send(jsonMessage);
      }

      this.onIceCandidate = function(candidate, wp) {
        console.log("Local candidate" + JSON.stringify(candidate));

        var message = {
          id: "onIceCandidate",
          candidate: candidate,
          name: name
        };
        sendMessage(message);
      };

      Object.defineProperty(this, "rtcPeer", { writable: true });

      this.dispose = function() {
        console.log("Disposing participant " + name);
        this.rtcPeer.dispose();
        container.parentNode.removeChild(container);
      };
    }

    return {
      state,
      send,
      connect,
      sendMessagePub,
      Participant,

      onParticipantLeft,
      register,
      sendMessage,
      receiveVideo,
      receiveVideoResponse,
      onNewParticipant,
      callResponse
    };
  }
};
</script>
