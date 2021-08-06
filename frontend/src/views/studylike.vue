<template>
  <h2>찜한 스터디</h2>
  <br>
  <div v-if="state.likeStudyListTest">
    <div v-for="i in state.likeStudyListTest.length" :key="i" @click="onClickStudyList(i)" class="study" >
      <study :studyData="state.likeStudyListTest[i-1]"/>
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

  setup() {
    const store = useStore();
    const state = reactive({
      likeStudyList : [],
      likeStudyListTest : [{
        study_id : 10,
        name : "찜한빡곰",
        content : "찜한빡곰 스터디는 원할한 스터디를 지원합니다.",
        population : 6,
        study_thumbnail : "thumbnail.jpg",
        joined_population: 3,
        deadline: "2021-08-23"
      },
      {
        study_id : 20,
        name : "찜한빡곰2 빡곰2 빡곰2 빡곰2 빡곰2 빡곰2 빡곰2",
        content : "찜한빡곰2 스터디는 원할한 스터디를 지원합니다.",
        population : 7,
        study_thumbnail : "thumbnail.jpg",
        joined_population: 3,
        deadline: "2021-08-23"
      },
      {
        study_id : 30,
        name : "찜한빡곰3",
        content : "찜한빡곰3 스터디는 원할한 스터디를 지원합니다. 빡곰3 스터디는 원할한 스터디를 지원합니다. 빡곰3 스터디는 원할한 스터디를 지원합니다. 빡곰3 스터디는 원할한 스터디를 지원합니다.",
        population : 8,
        study_thumbnail : "thumbnail.jpg",
        joined_population: 3,
        deadline: "2021-08-23"
      },
      {
        study_id : 40,
        name : "찜한빡곰4 빡곰4 빡곰4 빡곰4 빡곰4 빡곰4 빡곰4 빡곰4",
        content : "찜한빡곰4 스터디는 원할한 스터디를 지원합니다. 빡곰4 스터디는 원할한 스터디를 지원합니다. 빡곰4 스터디는 원할한 스터디를 지원합니다. 빡곰4 스터디는 원할한 스터디를 지원합니다.",
        population : 9,
        study_thumbnail : "thumbnail.jpg",
        joined_population: 3,
        deadline: "2021-08-23"
      },
      {
        study_id : 50,
        name : "찜한빡곰5 빡곰5 빡곰5 빡곰5 빡곰5 빡곰5 빡곰5 빡곰5",
        content : "찜한빡곰5 스터디는 원할한 스터디를 지원합니다. 빡곰5 스터디는 원할한 스터디를 지원합니다. 빡곰5 스터디는 원할한 스터디를 지원합니다. 빡곰5 스터디는 원할한 스터디를 지원합니다.",
        population : 10,
        study_thumbnail : "thumbnail.jpg",
        joined_population: 3,
        deadline: "2021-08-23"
      },{
        study_id : 50,
        name : "찜한빡곰56 빡곰6 빡곰6 빡곰6 빡곰6 빡곰6 빡곰6 빡곰6",
        content : "찜한빡곰6 스터디는 원할한 스터디를 지원합니다. 빡곰6 스터디는 원할한 스터디를 지원합니다. 빡곰6 스터디는 원할한 스터디를 지원합니다. 빡곰6 스터디는 원할한 스터디를 지원합니다.",
        population : 3,
        study_thumbnail : "thumbnail.jpg",
        joined_population: 2,
        deadline: "2021-08-23"
      }]
    })

    // 찜한 스터디 목록 가져오기
    const getLikeStudyList = function () {
      store
        .dispatch('root/requestLikeStudyList')
          .then(function(res) {
            console.log("가입한 스터디 목록 가져오기", res)
            state.joinStudyList = res.data.content
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

    const onClickStudyList = function (id) {
      router.push({
        name: 'studydetail-dialog',
        params: {
          studyId: state.likeStudyListTest[id-1].study_id
        }
      })
    }

    return { state, onClickStudyList, }
  }
};
</script>
