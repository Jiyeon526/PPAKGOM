<template>
  <h2>가입한 스터디</h2>
  <br>
  <div class="card-div" v-if="state.joinStudyListTest.length !== 0">
    <study
      class="study"
      v-for="i in state.joinStudyListTest.length" :key="i"
      @click="clickStudyList(i)"
      :studyData="state.joinStudyListTest[i-1]"
    />
  </div>
  <el-alert v-else
    title="가입하신 스터디가 없습니다. 새롭게 스터디를 생성하시거나 관심있는 스터디에 가입해주세요."
    type="error"
    center
    >
  </el-alert>
</template>

<style>
.card-div {
  display: inline-block;
}
.study {
  display: inline-block;

  min-width: 345px;
  max-width: 25%;
}
.el-card {
  width: 320px;
  margin: 0 8px;
  margin-bottom: 40px;
}
.el-card .image-wrapper {
  width: 100%;
  height: 190px;
}
.el-card .title {
  width: 100%;
  display:-webkit-box;
  font-weight: bold;
  word-wrap:break-word;
  -webkit-box-orient:vertical;
  overflow:hidden;
  text-overflow:ellipsis;
}
.el-card .bottom {
  margin-top: 10px;
  display:-webkit-box;
  word-wrap:break-word;
  -webkit-box-orient:vertical;
  overflow:hidden;
  text-overflow:ellipsis;
}

/* 테블릿, 모바일의 경우 한 줄 말줄임표시 title */
@media (max-width: 1269px) {
  .el-card .title {
    -webkit-line-clamp: 1;
    height:20px;
  }
}
/* 데스크탑의 경우 한 줄 말줄임표시 title */
@media (min-width: 1270px) {
  .el-card .title {
    -webkit-line-clamp: 1;
    height:20px;
  }
}

/* 테블릿, 모바일의 경우 두 줄 말줄임표시 */
@media (max-width: 1269px) {
  .el-card .bottom {
    -webkit-line-clamp: 2;
    height:42px;
  }
}
/* 데스크탑의 경우 세 줄 말줄임표시 */
@media (min-width: 1270px) {
  .el-card .bottom {
    -webkit-line-clamp: 3;
    height:60px;
  }
}

/* 인원 왼쪽 정렬 */
.popul-sort {
  display: flex;
  justify-content: left;
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
      joinStudyListTest : [{
        study_id : 1,
        name : "빡곰",
        content : "빡곰 스터디는 원할한 스터디를 지원합니다.",
        population : 5,
        study_thumbnail : "thumbnail.jpg",
        owner_id : 2
      },
      {
        study_id : 2,
        name : "빡곰2 빡곰2 빡곰2 빡곰2 빡곰2 빡곰2 빡곰2",
        content : "빡곰2 스터디는 원할한 스터디를 지원합니다.",
        population : 6,
        study_thumbnail : "thumbnail.jpg",
        owner_id : 2
      },
      {
        study_id : 3,
        name : "빡곰3",
        content : "빡곰3 스터디는 원할한 스터디를 지원합니다. 빡곰3 스터디는 원할한 스터디를 지원합니다. 빡곰3 스터디는 원할한 스터디를 지원합니다. 빡곰3 스터디는 원할한 스터디를 지원합니다.",
        population : 7,
        study_thumbnail : "thumbnail.jpg",
        owner_id : 2
      },
      {
        study_id : 4,
        name : "빡곰4 빡곰4 빡곰4 빡곰4 빡곰4 빡곰4 빡곰4 빡곰4",
        content : "빡곰4 스터디는 원할한 스터디를 지원합니다. 빡곰4 스터디는 원할한 스터디를 지원합니다. 빡곰4 스터디는 원할한 스터디를 지원합니다. 빡곰4 스터디는 원할한 스터디를 지원합니다.",
        population : 6,
        study_thumbnail : "thumbnail.jpg",
        owner_id : 2
      },
      {
        study_id : 5,
        name : "빡곰5 빡곰5 빡곰5 빡곰5 빡곰5 빡곰5 빡곰5 빡곰5",
        content : "빡곰5 스터디는 원할한 스터디를 지원합니다. 빡곰5 스터디는 원할한 스터디를 지원합니다. 빡곰5 스터디는 원할한 스터디를 지원합니다. 빡곰5 스터디는 원할한 스터디를 지원합니다.",
        population : 5,
        study_thumbnail : "thumbnail.jpg",
        owner_id : 2
      }]
    })

    // 가입한 스터디 목록 가져오기
    const getJoinStudyList = function () {
      store
        .dispatch('root/requestJoinStudyList')
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
      getJoinStudyList()
    });

    const clickstudyList = function (id) {
      router.push({
        name: 'studydetail-dialog',
        params: {
          studyId: state.joinStudyListTest[id-1].study_id
        }
      })
    }

    return { state, clickstudyList, }
  }
};
</script>
