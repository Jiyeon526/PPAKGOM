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
  <h4>회원님의 해시태그에 맞는 추천 스터디가 없습니다.</h4>
  <el-alert v-if="state.recommendStudyList.length === 0"
    title="회원님의 해시태그에 맞는 추천 스터디가 없습니다."
    type="info"
    effect="dark"
    center
  >
  </el-alert>
  <el-carousel v-else :interval="4000" type="card" height="200px">
    <el-carousel-item v-for="i in recommendStudyList.length" :key="i">
      <h3 class="medium">{{ recommendStudyList[i].name }}</h3>
    </el-carousel-item>
  </el-carousel>
  <ul v-if="state.studyList.length !== 0">
    <li v-for="i in state.studyList.length" @click="clickStudyList(i)" class="infinite-list-item" :key="i" >
      <study :studyData="state.studyList[i-1]" />
    </li>
  </ul>
    <el-alert v-else
      title="존재하는 스터디가 없습니다. 새롭게 스터디를 생성하시거나 다른 제목으로 스터디를 검색해주세요."
      type="error"
      center
      >
    </el-alert>
</template>
<style>
.search-bar {
  display: flex;
}

.option {
  width: 20%;
}

.search-field {
  width: 80%;
}

.infinite-list {
  padding-left: 0;
  max-height: calc(100% - 35px);
}

@media (min-width: 701px) and (max-width: 1269px) {
  .infinite-list {
    min-width: 700px;
  }
}

@media (min-width: 1270px) {
  .infinite-list {
    min-width: 1021px;
  }
}

.infinite-list .infinite-list-item {
  min-width: 345px;
  max-width: 25%;
  display: inline-block;
  cursor: pointer;
}

/* carousel */
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
import { ElMessage } from 'element-plus'

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
      recommendStudyList : [],
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

    const clickstudyList = function (id) {
      router.push({
        name: 'studydetail-dialog',
        params: {
          studyId: state.studyList[id - 1].id
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



    return { state, clickstudyList, }
  }
}
</script>
