<template>
  <h1 style="font-size:35px;">스터디 홈</h1>
  <div class="study-home-div">
    <div @click="onClickAttitude" style="width: 50%;  margin: 10px;">
      <h4>차트</h4>
      <bar-chart
        :data="state.cdata"
        xtitle="출석점수"
        ytitle="스터디원"
        :colors="state.tcolor"
        empty="출석 경험이 없군요"
      ></bar-chart>
      <div @click="onClickScore">
        <h4>점수</h4>
        <line-chart :data="state.ccdata" empty="우리 문제 풀러 갈까요?" />
      </div>
    </div>
    <div @click="onClicSchedule" style="width: 50%;">
      <h4 style="margin-bottom:50px">스터디 일정</h4>
      <Studyschedulecomponent
        :studyId="state.studyId"
        style=" vertical-align:top;"
      />
    </div>
  </div>
</template>

<script>
import Studyschedulecomponent from "@/views/studydetail/studyschedulecomponent";
import { onMounted, reactive, computed } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";

export default {
  name: "studyhome",
  components: {
    Studyschedulecomponent
  },
  setup() {
    const store = useStore();
    const router = useRouter();
    const state = reactive({
      studyId: computed(() => store.getters["root/getStudypk"]),
      studypk: 0,
      userpk: 0,
      cdata: [],
      ccdata: [],
      scoredata: [],
      temp: [],
      color: [],
      tcolor: [],
      rresults: []
    });
    const onClickAttitude = () => {
      store.commit("root/setSelectOption", "studyattitude");
      router.push({
        name: "studyattitude"
      });
    };
    const onClicSchedule = () => {
      store.commit("root/setSelectOption", "studyschedule");
      router.push({
        name: "studyschedule"
      });
    };
    const onClickWorkbook = () => {
      store.commit("root/setSelectOption", "studyworkbook");
      router.push({
        name: "studyworkbook"
      });
    };
    const onClickScore = () => {
      store.commit("root/setSelectOption", "studyscore");
      router.push({
        name: "studyscore"
      });
    };
    // 페이지 진입시 불리는 훅
    onMounted(() => {
      state.studypk = store.getters["root/getStudypk"];
      store.dispatch("root/requestStudyAttend", state.studypk).then(res => {
        res.data.forEach(e => {
          let ss = [];
          ss.push(e.user_name);
          ss.push(e.attended);
          state.temp.push(ss);
          state.color.push("#" + Math.floor(Math.random() * 1000));
        });
        converse();
      });

      state.studypk = store.getters["root/getStudypk"];
      state.userpk = store.getters["root/getUserpk"];
      store.dispatch("root/requestScore", state.studypk).then(res => {
        res.data.forEach(element => {
          let data = "{";
          element.data.forEach(e => {
            let test = e.test_title;
            data += `"${test}":"${e.score}",`;
          });
          data = data.slice(0, -1);
          data += "}";
          // data = '{"test":"20"}';
          let cdata = JSON.parse(data);
          state.scoredata.push({
            name: element.name,
            data: cdata
          });
          // state.scoredata = JSON.parse(state.scoredata);
          conver();
        });
      });
    });

    const conver = function() {
      state.ccdata = state.scoredata;
    };

    // 출석 데이터 교환
    const converse = function() {
      state.cdata = state.temp;
      state.tcolor = state.color;
    };

    return {
      state,
      onClickScore,
      onClickAttitude,
      onClicSchedule,
      onClickWorkbook
    };
  }
};
</script>
<style scoped>
.study-home-div {
  width: 100%;
  height: auto;
  display: flex;
  justify-content: space-evenly;
  align-items: flex-start;
  margin-top: 50px;
}
</style>
