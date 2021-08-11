<template>
  <h2>찜한 스터디</h2>
  <br>
  <div v-if="state.likeStudyList.length !== 0">
    <div v-for="i in state.likeStudyList.length" :key="i" @click="onClickStudyList(i)" class="study" >
      <study :studyData="state.likeStudyList[i-1]"/>
    </div>
  </div>
  <el-alert v-else
    title="찜한 스터디가 없습니다. 관심있는 스터디를 찜해주세요."
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

  setup(props, { emit }) {
    const store = useStore();
    const state = reactive({
      likeStudyList : [],
    })

    // 찜한 스터디 목록 가져오기
    const getLikeStudyList = function () {
      store
        .dispatch('root/requestLikeStudyList')
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
      getLikeStudyList()
    });

    const onClickStudyList = (id) => {
      const selectStudy = state.likeStudyList[id-1]
      emit("openStudydetailDialog", selectStudy);
    }

    return { state, onClickStudyList, }
  }
};
</script>
