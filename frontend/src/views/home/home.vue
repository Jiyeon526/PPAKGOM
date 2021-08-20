<template>
  <h1 style="font-size:35px;">스터디 모집</h1>
  <div class="search-bar">
    <el-dropdown trigger="click" Button style="width:20%">
      <el-button class="el-dropdown-link" style="width:100%;">
        <span :class="state.selectColor">{{ state.label }}</span>
        <i class="el-icon-arrow-down"></i>
      </el-button>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item
            v-for="option in state.options"
            :key="option.value"
            @click="onClickSearchType(option)"
          >
            {{ option.label }}
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
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
  <el-carousel v-else height="300px">
    <el-carousel-item
      v-for="i in state.recommendStudyList.length"
      :key="i"
      @click="onClickRecommendStudyList(i)"
    >
      <div
        style="display:flex; align-items:center; margin-left:5%; margin-right:15%"
      >
        <StudyCarousel
          :studyData="state.recommendStudyList[i - 1]"
          style="width:300px"
        />
        <div style="width:40%; height:30%; text-align:center">
          <div>
            <h2 style="margin:30px">
              {{ state.recommendStudyList[i - 1].name }}
            </h2>
            <h4>
              모집 인원 :
              {{ state.recommendStudyList[i - 1].joined_population }} /
              {{ state.recommendStudyList[i - 1].population }}
            </h4>
            <h4>
              관심 분야 :
              {{ state.recommendStudyList[i - 1].interest.join(", ") }}
            </h4>
            <h4>마감 날짜 : {{ state.recommendStudyList[i - 1].deadline }}</h4>
          </div>
        </div>
        <div style="margin-top:auto; margin-bottom:auto;">
          <el-progress
            type="dashboard"
            :show-text="false"
            :percentage="state.recommendStudyList[i - 1].temperature"
            style="position:relative; z-index: 1;"
          >
          </el-progress>
          <p style="position:relative; top:-90px; z-index: 2;">
            {{ state.recommendStudyList[i - 1].temperature }}℃
          </p>
          <h4>열정도</h4>
        </div>
      </div>
    </el-carousel-item>
  </el-carousel>
  <ul v-if="state.studyList.length !== 0" class="ul-class">
    <li
      v-for="i in state.studyList.length"
      :key="i"
      @click="onClickStudyList(i)"
      class="li-class"
    >
      <study :studyData="state.studyList[i - 1]" style="width:100%" />
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

.img-center {
  display: block;
  margin-left: auto;
  margin-right: auto;
}

.base-color {
  color: #c0c4cc;
}

.select-color {
  color: black;
}

.search-bar {
  display: flex;
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
  background-color: rgba(255, 255, 255, 0);
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: rgba(255, 255, 255, 0);
}

.el-progress {
  position: relative;
  line-height: 1;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
}
</style>
<script>
import Study from "./components/study";
import StudyCarousel from "./components/studycarousel";
import { onMounted, reactive, computed } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { ElMessage } from "element-plus";

export default {
  name: "Home",

  components: {
    Study,
    StudyCarousel
  },

  setup(props, { emit }) {
    const store = useStore();
    const router = useRouter();
    const state = reactive({
      isLoggedIn: computed(() => store.getters["root/isLoggedIn"]),
      studyList: [],
      recommendStudyList: [],
      searchValue: "",
      searchType: "",
      isCardClick: true,
      likeStudy: false,
      label: "선택",
      selectColor: "base-color",
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
      studyData: [],
      temp: []
    });

    const onClickRecommendStudyList = id => {
      const selectStudy = state.recommendStudyList[id - 1];
      emit("openStudydetailDialog", selectStudy);
    };

    const onClickStudyList = id => {
      const selectStudy = state.studyList[id - 1];
      emit("openStudydetailDialog", selectStudy);
    };

    // 방 목록 리스트 가져오기
    const getStudyList = function() {
      store
        .dispatch("root/requestStudyList", {})
        .then(function(res) {
          state.studyList = res.data.studyResult;
        })
        .catch(function(err) {
          ElMessage({
            type: "error",
            message: err.message
          });
        });
    };

    // 추천 리스트 가져오기
    const getRecommendStudyList = function() {
      store
        .dispatch("root/requestRecommendStudyList", {})
        .then(function(res) {
          state.recommendStudyList = res.data.studyResult;
        })
        .catch(function(err) {
          ElMessage({
            type: "error",
            message: err.message
          });
        });
    };

    onMounted(() => {
      if (state.isLoggedIn) {
        getRecommendStudyList();
      }
      getStudyList();
    });

    const onClickSearchType = option => {
      state.label = option.label;
      state.searchType = option.value;
      state.selectColor = "select-color";
    };

    // 검색한 내용으로 스터디 목록 가져오기
    const searchStudy = function() {
      let cleanValue = state.searchValue.trim();
      if (cleanValue !== "") {
        if (!state.searchType) {
          ElMessage({
            type: "info",
            message: "검색하려는 분야를 선택해주세요."
          });
        } else {
          store
            .dispatch("root/requestSearchStudyList", {
              option: state.searchType,
              searchValue: cleanValue
            })
            .then(function(res) {
              state.studyList = [];
              state.temp = res.data.studyResult;
            })
            .then(() => {
              state.studyList = state.temp;
            })
            .catch(function(err) {
              ElMessage({
                type: "error",
                message: err.message
              });
            });
        }
      } else {
        state.searchValue = "";
      }
    };

    return {
      state,
      onClickStudyList,
      onClickRecommendStudyList,
      searchStudy,
      onClickSearchType
    };
  }
};
</script>
