<template>
  <h1 style="font-size:35px;">스터디 모집</h1>
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
  <el-carousel v-else :interval="4000" type="card" height="300px" >
    <el-carousel-item v-for="i in state.recommendStudyList.length" :key="i" @click="onClickRecommendStudyList(i)" style="width:300px">
      <StudyCarousel :studyData="state.recommendStudyList[i-1]" style="width:100%"/>
      <!-- <el-image style="width: 300px; height: 300px"
        :src="state.uri[i-1]"
        :fit="fit"
        alt="PPAKGOM"
      >
      </el-image> -->
    </el-carousel-item>
      <!-- <p>'https://localhost:8443/' + state.recommendStudyList[i-1].study_thumbnail</p> -->
  </el-carousel>
  <!-- <ul v-if="state.studyList.length !== 0" style="display:flex; flex-wrap: wrap; justify-content: flex-start;"> -->
  <ul v-if="state.studyList.length !== 0" class="ul-class">
    <li
      v-for="i in state.studyList.length"
      :key="i"
      @click="onClickStudyList(i)"
      class="li-class"
    >
      <study :studyData="state.studyList[i-1]" style="width:100%"/>
    </li>
  </ul>
  <el-alert
    v-else
    title="존재하는 스터디가 없습니다. 새롭게 스터디를 생성하시거나 다른 제목으로 스터디를 검색해주세요."
    type="error"
    center
  >
  </el-alert>
</template>
<style scoped>
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

/* .study {
  display: flex;
  justify-content: flex-start;
  margin-left: auto;
  margin-right: auto;
} */
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
  color: #d3dce6;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: rgba( 255, 255, 255, 0 );
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: rgba( 255, 255, 255, 0 );
}
</style>
<script>
import Study from "./components/study"
import StudyCarousel from "./components/studycarousel"
import { onMounted, reactive, computed } from "vue"
import { useRouter } from "vue-router"
import { useStore } from "vuex"
import { ElMessage } from "element-plus"
export default {
  name: "Home",

  components: {
    Study,
    StudyCarousel,
  },

  setup(props, { emit }) {
    const store = useStore()
    const router = useRouter()
    const state = reactive({
      isLoggedIn: computed(() => store.getters["root/isLoggedIn"]),
      studyList: [],
      recommendStudyList: [],
      searchValue: "",
      searchType: "",
      isCardClick: true,
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
      ],
      uri: [],
      studyData: ''
    })

    const onClickRecommendStudyList = (id) => {
      const selectStudy = state.recommendStudyList[id-1]
      emit("openStudydetailDialog", selectStudy);
    }


    const onClickStudyList = (id) => {
      const selectStudy = state.studyList[id-1]
      emit("openStudydetailDialog", selectStudy)
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
        })
        .catch(function(err) {
          console.log('추천 리스트 응답 에러', err)
        })
    }

    // const getImage = (thumbnail) => {
    //   let studyData = thumbnail
    //   console.log("여기 확인", studyData);
    //   var name;
    //   if (
    //     studyData.split("\\").length > studyData.split("/").length
    //   ) {
    //     name = studyData.split("\\");
    //   } else {
    //     name = studyData.split("/");
    //   }
    //   console.log("네임",name);
    //   //name = "9-kakao.jpg";
    //    axios({
    //     url: `https://localhost:8443/api/v1/study/${name[2]}/download`,
    //     method: "GET",
    //     responseType: "blob"
    //   }).then(res => {
    //     console.log("여여여",URL.createObjectURL(res.data))
    //     state.uri.push(URL.createObjectURL(res.data));
    //   });
    // }

    onMounted(() => {
      if (state.isLoggedIn) {
        getRecommendStudyList()
        // store
        // .dispatch('root/requestRecommendStudyList', {
        // })
        // .then(function(res) {
        //   console.log('추천 리스트 응답 결과', res)
        //   state.recommendStudyList = res.data.studyResult
        //   console.log("데이터확인", state.recommendStudyList)
        //   console.log("길이 확인",state.recommendStudyList.length)
        //   // for (let i = 0; i < state.recommendStudyList.length; i++) {
        //     getImage(state.recommendStudyList[0].study_thumbnail)
        //     console.log("여기")
        //   // }
        // })
        // .catch(function(err) {
        //   console.log('추천 리스트 응답 에러', err)
        // })
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

    return { state, onClickStudyList, onClickRecommendStudyList, searchStudy, }
  }
}
</script>
