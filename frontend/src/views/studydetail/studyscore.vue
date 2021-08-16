<template>
  <h2>스터디 점수그래프</h2>
  <div v-if="state.scoredata">
    <line-chart :data="state.cdata" loading="loading" />
    {{ state.cdata }} {{ state.scoredata }}{{ state.data }}
  </div>
</template>

<script>
import { reactive, computed, watch, onMounted, onBeforeMount } from "vue";
import { useStore } from "vuex";

export default {
  name: "studyscore",
  setup() {
    const store = useStore();
    const state = reactive({
      studypk: 0,
      userpk: 0,
      cdata: [],
      data: [
        {
          //
          //
          name: "people1",
          data: {
            test1: "20"

            // "2017-01-02 00:00:00 -0800": 90,
            // "2017-01-03 00:00:00 -0800": 72,
            // "2017-01-04 00:00:00 -0800": 70,
            // "2017-01-05 00:00:00 -0800": 65,
            // "2017-01-06 00:00:00 -0800": 80,
            // "2017-01-07 00:00:00 -0800": 98,
            // "2017-01-08 00:00:00 -0800": 85,
            // "2017-01-09 00:00:00 -0800": 45
          }
        }
        // ,
        // {
        //   name: "people2",
        //   data: {
        //     문제집1: 44,
        //     문제집1: 8,
        //     문제집1: 80
        //     // "2017-01-02 00:00:00 -0800": 55,
        //     // "2017-01-03 00:00:00 -0800": 66,
        //     // "2017-01-04 00:00:00 -0800": 77,
        //     // "2017-01-05 00:00:00 -0800": 88,
        //     // "2017-01-06 00:00:00 -0800": 99,
        //     // "2017-01-07 00:00:00 -0800": 55,
        //     // "2017-01-08 00:00:00 -0800": 88,
        //     // "2017-01-09 00:00:00 -0800": 86
        //   }
        // },
        // {
        //   name: "people3",
        //   data: {
        //     문제집1: 65,
        //     문제집1: 65,
        //     문제집1: 65
        //     // "2017-01-02 00:00:00 -0800": 78,
        //     // "2017-01-03 00:00:00 -0800": 85,
        //     // "2017-01-04 00:00:00 -0800": 88,
        //     // "2017-01-05 00:00:00 -0800": 92,
        //     // "2017-01-06 00:00:00 -0800": 95,
        //     // "2017-01-07 00:00:00 -0800": 70,
        //     // "2017-01-08 00:00:00 -0800": 75,
        //     // "2017-01-09 00:00:00 -0800": 65
        //   }
        // }
      ],
      scoredata: [],
      cdata: []
    });
    // watch(
    //   () => state.scoredata,
    //   scoredata => {
    //     state.cdata = scoredata;
    //     re;
    //   }
    // );
    // 페이지 진입시 불리는 훅
    onMounted(() => {
      state.studypk = store.getters["root/getStudypk"];
      state.userpk = store.getters["root/getUserpk"];
      store.dispatch("root/requestScore", state.studypk).then(res => {
        console.log(res.data);
        res.data.forEach(element => {
          console.log(element);
          let data = "{";
          element.data.forEach(e => {
            console.log("문제점수", e);
            let test = e.test_title;
            data += `"${test}":"${e.score}",`;
          });
          data = data.slice(0, -1);
          data += "}";
          // data = '{"test":"20"}';
          console.log("모인데이터", data);
          let cdata = JSON.parse(data);
          state.scoredata.push({
            name: element.name,
            data: cdata
          });
          // state.scoredata = JSON.parse(state.scoredata);
          console.log("결과물", state.scoredata);
          console.log("비교물", state.data);
          conver();
        });
      });
      console.log("확인", state.scoredata);
    });
    const conver = function() {
      state.cdata = state.scoredata;
    };
    return { state, conver };
  }
};
</script>
