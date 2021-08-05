<template>
  <h1>스터디 모집</h1>
  <div class="search-bar">
    <el-select class="option" v-model="state.searchType" placeholder="Select">
      <el-option
        v-for="item in state.options"
        :key="item.value"
        :label="item.label"
        :value="item.value">
      </el-option>
    </el-select>
    <div class="search-field">
      <el-input
        placeholder="화상 컨퍼런스 제목 검색"
        v-model="state.searchValue"
        @keyup.enter="searchStudy"
      >
        <template #append>
          <el-button
            icon="el-icon-search"
            @click="searchStudy"
          ></el-button>
        </template>
      </el-input>
    </div>
  </div>
  <br>
  <h4 v-if="state.recommendStudyListTest.length === 0">회원님의 해시태그에 맞는 추천 스터디가 없습니다.</h4>
  <el-carousel v-else :interval="4000" type="card" height="200px">
    <el-carousel-item v-for="i in state.recommendStudyListTest.length" :key="i">
      <h3 class="medium">{{ state.recommendStudyListTest[i-1].name }}</h3>
    </el-carousel-item>
  </el-carousel>
  <div v-if="state.studyListTest">
    <div v-for="i in state.studyListTest.length" :key="i" @click="onClickStudyList(i)" class="study" >
      <study :studyData="state.studyListTest[i-1]"/>
    </div>
  </div>
  <el-alert v-else
    title="존재하는 스터디가 없습니다. 새롭게 스터디를 생성하시거나 다른 제목으로 스터디를 검색해주세요."
    type="error"
    center
  >
  </el-alert>
</template>
<style>
.study {
  display: inline-block;
}
.search-bar {
  display: flex;
}

.option {
  width: 20%;
}

.search-field {
  width: 80%;
}

/* carousel */
.el-carousel {
  margin: 50px 0px;
}

.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
</style>
<script>
import Study from './components/study'
import { onMounted, reactive, computed} from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from "vuex"

export default {
  name: 'Home',

  components: {
    Study,
  },

  setup () {
    const store = useStore()
    const router = useRouter()
    const state = reactive({
      isLoggedIn: computed(() => store.getters["root/isLoggedIn"]),
      studyList : [],
      // Test 코드
      studyListTest : [{
        study_id: 1,
        interest: ["프로그래밍","개발자"],
        name: "빡곰1",
        content: "빡곰 스터디는 원할한 스터디를 지원합니다.",
        population: 5,
        study_thumbnail: "thumbnail.jpg",
        joined_population: 3,
        deadline: "2021-08-23"
      },{
        study_id: 2,
        interest: ["프로그래밍","개발자"],
        name: "빡곰2",
        content: "빡곰 스터디는 원할한 스터디를 지원합니다.",
        population: 5,
        study_thumbnail: "thumbnail.jpg",
        joined_population: 2,
        deadline: "2021-08-23"
      },{
        study_id: 3,
        interest: ["프로그래밍","개발자"],
        name: "빡곰3",
        content: "빡곰 스터디는 원할한 스터디를 지원합니다.",
        population: 5,
        study_thumbnail: "thumbnail.jpg",
        joined_population: 1,
        deadline: "2021-08-23"
      },{
        study_id: 4,
        interest: ["프로그래밍","개발자"],
        name: "빡곰4",
        content: "빡곰 스터디는 원할한 스터디를 지원합니다.",
        population: 5,
        study_thumbnail: "thumbnail.jpg",
        joined_population: 4,
        deadline: "2021-08-23"
      }],
      recommendStudyList : [],
      // Test 코드
      recommendStudyListTest : [{
        study_id: 11,
        interest: ["프로그래밍","개발자"],
        name: "추천1",
        content: "빡곰 스터디는 원할한 스터디를 지원합니다.",
        population: 5,
        study_thumbnail: "thumbnail.jpg",
        joined_population: 3,
        deadline: "2021-08-23"
      },{
        study_id: 12,
        interest: ["프로그래밍","개발자"],
        name: "추천2",
        content: "빡곰 스터디는 원할한 스터디를 지원합니다.",
        population: 5,
        study_thumbnail: "thumbnail.jpg",
        joined_population: 2,
        deadline: "2021-08-23"
      },{
        study_id: 13,
        interest: ["프로그래밍","개발자"],
        name: "추천3",
        content: "빡곰 스터디는 원할한 스터디를 지원합니다.",
        population: 5,
        study_thumbnail: "thumbnail.jpg",
        joined_population: 1,
        deadline: "2021-08-23"
      }],
      searchValue : '',
      searchType : '',
      studyId : null,
      name : '',
      interest : '',
      options: [{
          value: 1,
          label: '방 번호'
        }, {
          value: 2,
          label: '방 이름'
        }, {
          value: 3,
          label: '해시태그'
        }],
    })

    const onClickStudyList = function (id) {
      router.push({
        name: 'studydetail-dialog',
        params: {
          studyId: state.studyListTest[id - 1].study_id
        }
      })
    }

    // 방 목록 리스트 가져오기
    const getStudyList = function () {
      store
        .dispatch('root/requestStudyList', {
        })
        .then(function(res) {
          console.log('스터디 목록 받아오기', state.studyList)
          state.studyList = res.data.content  // 데이터 확인 필요
        })
        .catch(function(err) {
          console.log('스터디 목록 받아오기 에러', err)
        })
    }

    // 추천 리스트 가져오기
    const getRecommendStudyList = function () {
      store
        dispatch('root/requestRecommendStudyList', {
        })
        .then(function(res) {
          console.log('추천 리스트 응답 결과', res)
          state.recommendList = res.data.content    // 데이터 확인 필요
        })
        .catch(function(err) {
          console.log('추천 리스트 응답 에러', err)
        })
    }

    onMounted(() => {
      if (state.isLoggedIn) {
        getRecommendStudyList()
      }
      getStudyList()
    })

    return { state, onClickStudyList }
  }
}
</script>
