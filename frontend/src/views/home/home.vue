<template>
  <h1>스터디 모집</h1>
  <div class="search-bar">
    <el-select class="option" v-model="state.searchType" placeholder="Select">
      <el-option
        v-for="item in state.options"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      >
      </el-option>
    </el-select>
    <div class="search-field">
      <el-input
        placeholder="화상 컨퍼런스 제목 검색"
        v-model="state.searchValue"
        @keyup.enter="searchStudy"
      >
        <template #append>
          <el-button icon="el-icon-search" @click="searchStudy"></el-button>
        </template>
      </el-input>
    </div>
  </div>
  <br />
  <h4 v-if="state.recommendStudyList.length === 0">
    회원님의 해시태그에 맞는 추천 스터디가 없습니다.
  </h4>
  <el-carousel v-else :interval="4000" type="card" height="200px">
    <el-carousel-item v-for="i in state.recommendStudyList.length" :key="i">
      <el-image style="width: 100%; height: 190px"
        :src="state.recommendStudyList[i-1].study_thumbnail"
        :fit="fit"
        alt="PPAKGOM"
      >
      </el-image>
    </el-carousel-item>
  </el-carousel>
  <div v-if="state.studyList.length !== 0">
    <div
      v-for="i in state.studyList.length"
      :key="i"
      @click="onClickStudyList(i)"
      class="study"
    >
      <study :studyData="state.studyList[i-1]" />
    </div>
  </div>
  <el-alert
    v-else
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

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>
<script>
import Study from "./components/study"
import { onMounted, reactive, computed } from "vue"
import { useRouter } from "vue-router"
import { useStore } from "vuex"
import { ElMessage } from "element-plus"
export default {
  name: "Home",

  components: {
    Study
  },

  setup() {
    const store = useStore()
    const router = useRouter()
    const state = reactive({
      isLoggedIn: computed(() => store.getters["root/isLoggedIn"]),
      studyList: [],
      recommendStudyList: [],
      searchValue: "",
      searchType: "",
      addRecommendStudy: {
        study_id: 0,
        interest: ["프로그래밍","개발자"],
        name: "관심분야를 더 등록해주세요",
        content: "빡곰 스터디는 원할한 스터디를 지원합니다.",
        population: 5,
        study_thumbnail: require('@/assets/images/ppakgom.png'),
        joined_population: 3,
        deadline: "2021-08-23"
      },
      options: [
        {
          value: 1,
          label: "방 번호"
        },
        {
          value: 2,
          label: "방 이름"
        },
        {
          value: 3,
          label: "해시태그"
        }
      ]
    })

    const onClickStudyList = function(id) {
      router.push({
        name: "studydetail-dialog",
        params: {
          studyId: state.studyListTest[id - 1].study_id
        }
      })
    }

    // 방 목록 리스트 가져오기
    const getStudyList = function() {
      store
        .dispatch("root/requestStudyList", {})
        .then(function(res) {
          state.studyList = res.data.studyResult
          console.log("스터디 목록 받아오기", state.studyList)
        })
        .catch(function(err) {
          console.log("스터디 목록 받아오기 에러", err)
        })
    }

    // 추천 리스트 가져오기
    const getRecommendStudyList = function() {
      store
        .dispatch('root/requestRecommendStudyList', {
        })
        .then(function(res) {
          console.log('추천 리스트 응답 결과', res)
          state.recommendStudyList = res.data.studyResult
          // 받아온 study_thumbnail에 'https://localhost:8443/'를 붙여서 주소로 만들고 src로 넣어준다.
          // 1개나 2개의 데이터를 받아오는 경우 기본 이미지를 출력하기 위해
          const recommendStudIndex = state.recommendStudyList.length
          let cnt = 0
          while (cnt < recommendStudIndex) {
            state.recommendStudyList[cnt].study_thumbnail = 'https://localhost:8443/' + state.recommendStudyList[cnt].study_thumbnail
            cnt += 1
          }
          if (0 < state.recommendStudyList.length < 3) {
            state.recommendStudyList.push(state.addRecommendStudy)
            if (0 < state.recommendStudyList.length < 3) {
              state.recommendStudyList.push(state.addRecommendStudy)
            }
          }
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

    // 검색한 내용으로 스터디 목록 가져오기
    const searchStudy = function() {
      let cleanValue = state.searchValue.trim();
      console.log(cleanValue)
      if (cleanValue !== "") {
        if (!state.searchType) {
          ElMessage({
            type: "info",
            message: "검색하려는 분야를 선택해주세요"
          })
        } else {
          store
            .dispatch("root/requestSearchStudyList", {
              option: state.searchType,
              searchValue: cleanValue
            })
            .then(function(res) {
              state.studyList = res.data.studyResult
              console.log("검색 스터디 목록 받아오기", state.studyList)
            })
            .catch(function(err) {
              console.log("검색 스터디 목록 받아오기 에러", err)
            })
        }
      } else {
        state.searchValue = ""
      }
    }

    return { state, onClickStudyList, searchStudy }
  }
}
</script>
