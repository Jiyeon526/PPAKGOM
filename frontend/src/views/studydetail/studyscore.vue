<template>
  <h2>스터디 점수그래프</h2>
  <line-chart :data="state.cdata" empty="우리 문제 풀러 갈까요?" />

  <div
    v-for="result in state.results"
    :key="result"
    style="display:inline-block;"
  >
    <h1>{{ result.name }}</h1>
    <el-table :data="result.data" style="width: 300px ">
      <el-table-column prop="test_title" label="문제집" width="120">
      </el-table-column>
      <el-table-column prop="score" label="맞은 갯수" width="180" sortable>
        <template #default="scope">
          <div v-if="scope.row.score > 90">
            {{ scope.row.score }}
          </div>
          <div v-else-if="scope.row.score > 50">
            {{ scope.row.score }}
          </div>
          <div v-else>
            {{ scope.row.score }}
          </div>
        </template>
      </el-table-column>
    </el-table>
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
      results: [],
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
        state.results = res.data;
        res.data.forEach(element => {
          console.log(element);
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
      state.cdata = state.scoredata;
    };
    return { state, conver };
  }
};
</script>
