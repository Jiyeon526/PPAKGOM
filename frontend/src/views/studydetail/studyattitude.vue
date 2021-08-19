<template>
  <h2>스터디 출석현황</h2>
  <bar-chart
    :data="state.cdata"
    xtitle="출석점수"
    ytitle="스터디원"
    :colors="state.tcolor"
    empty="출석 경험이 없군요"
  ></bar-chart>

  <div
    v-for="result in state.results"
    :key="result"
    style="display:inline-block;"
  >
    <h1>{{ result.user_name }}</h1>
    <el-table
      :data="result.attendList"
      :default-sort="{ prop: 'date', order: 'descending' }"
      style="width: 300px "
    >
      <el-table-column prop="study_plan_date" label="Date" sortable width="180">
      </el-table-column>
      <el-table-column prop="attend" label="참가여부" width="180">
        <template #default="scope">
          <div v-if="scope.row.attend == true">
            <i class="el-icon-check">참석</i>
          </div>
          <div v-else><i class="el-icon-close">불참</i></div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { onMounted, reactive } from "vue";
import { useStore } from "vuex";

export default {
  name: "studyattitude",
  setup() {
    const store = useStore();
    const state = reactive({
      studypk: 0,
      cdata: [],
      temp: [],
      color: [],
      tcolor: [],
      results: []
    });
    // 페이지 진입시 불리는 훅
    onMounted(() => {
      state.studypk = store.getters["root/getStudypk"];
      store.dispatch("root/requestStudyAttend", state.studypk).then(res => {
        console.log(res.data);
        state.results = res.data;
        res.data.forEach(e => {
          let ss = [];
          ss.push(e.user_name);
          ss.push(e.attended);
          state.temp.push(ss);
          state.color.push("#" + Math.floor(Math.random() * 1000));
        });
        converse();
        console.log(state.cdata);
      });
    });

    const converse = function() {
      state.cdata = state.temp;
      state.tcolor = state.color;
      console.log(state.tcolor);
    };
    return { state };
  }
};
</script>
