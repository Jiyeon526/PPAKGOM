<template>
  <h2>가입한 스터디</h2>
  <br>
  <div v-if="state.joinStudyList.length !== 0">
    <div v-for="i in state.joinStudyList.length" :key="i" @click="onClickStudyList(i)" class="study" >
      <study :studyData="state.joinStudyList[i-1]"/>
    </div>
  </div>
  <el-alert v-else
    title="가입하신 스터디가 없습니다. 새롭게 스터디를 생성하시거나 관심있는 스터디에 가입해주세요."
    type="error"
    center
    >
  </el-alert>
</template>

<style>
.study {
  display: inline-block;
}
</style>
<script>
import Study from "./home/components/study"
import { onMounted, reactive } from "vue";
import { useStore } from "vuex";

export default {
  name: "StudyJoin",

  components : {
    Study,
  },

  setup() {
    const store = useStore();
    const state = reactive({
      joinStudyList : [],
    })

    // 가입한 스터디 목록 가져오기
    const getJoinStudyList = function () {
      store
        .dispatch('root/requestJoinStudyList')
          .then(function(res) {
            console.log("가입한 스터디 목록 가져오기", res)
            state.joinStudyList = res.data.studyResult
          })
          .catch(function(err) {
            console.log("가입한 스터디 목록 가져오기 에러", err)
          })
    }


    // 페이지 진입시 불리는 훅
    onMounted(() => {
      store.commit("root/setMenuActiveMenuName", "join");
      getJoinStudyList()
    });

    const onClickStudyList = function (id) {
      router.push({
        name: 'studydetail-dialog',
        params: {
          studyId: state.joinStudyList[id-1].study_id
        }
      })
    }

    return { state, onClickStudyList, }
  }
};
</script>
