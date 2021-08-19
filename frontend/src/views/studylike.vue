<template>
  <h2>찜한 스터디</h2>
  <br>
  <ul v-if="state.likeStudyList.length !== 0" class="ul-class">
    <li v-for="i in state.likeStudyList.length" :key="i" @click="onClickStudyList(i)" class="li-class" >
      <study :studyData="state.likeStudyList[i-1]"/>
    </li>
  </ul>
  <el-alert v-else
    title="찜한 스터디가 없습니다. 관심있는 스터디를 찜해주세요."
    type="error"
    center
    >
  </el-alert>
</template>

<style>
.ul-class {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, max-content));
  grid-gap: 16px;
  justify-content: center;
  padding: initial;
}
.li-class {
  list-style-type: none;
  padding: 5px;
  width: 300px;
}
</style>
<script>
import Study from "./home/components/study"
import { onMounted, reactive } from "vue";
import { useStore } from "vuex";
import { ElMessage } from "element-plus";

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
            state.likeStudyList = res.data.studyResult
          })
          .catch(function(err) {
            ElMessage({
              type: "error",
              message: err.message
            })
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
