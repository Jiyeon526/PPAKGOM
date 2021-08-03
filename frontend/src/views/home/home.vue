<template>
  <ul v-if="state.responseConference" class="infinite-list" v-infinite-scroll="load" style="overflow:auto">
    <li class="grid-content el-button-group">
      <el-button v-if="state.isSortTime === 'basic'" type="primary" icon="el-icon-sort" @click="clickFirstTimeSort">시간</el-button>
      <el-button v-else-if="state.isSortTime" type="primary" icon="el-icon-sort-up" @click="clickTimeSort">시간</el-button>
      <el-button v-else type="primary" icon="el-icon-sort-down" @click="clickTimeSort">시간</el-button>
      <el-button v-if="state.isSortTitle === 'basic'" type="primary" icon="el-icon-sort" @click="clickFirstTitleSort">제목</el-button>
      <el-button v-else-if="state.isSortTitle" type="primary" icon="el-icon-sort-up" @click="clickTitleSort">제목</el-button>
      <el-button v-else type="primary" icon="el-icon-sort-down" @click="clickTitleSort">제목</el-button>
    </li>
    <li v-for="i in state.responseConference.length" @click="clickConference(i)" class="infinite-list-item" :key="i" >
      <conference :conferenceData="state.responseConference[i-1]" />
    </li>
  </ul>
  <h2 v-else>데이터 없음
    <el-alert
      title="error alert"
      type="error">
    </el-alert>
  </h2>
</template>
<style>
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

/* .btn-right {
  display: flex;
  justify-content: right;
} */

.el-button-group {
  display: flex;
  justify-content: flex-end;
  /* float: right; */
  margin: 10px;
}

.el-button-group > .el-button:first-child {
  border-radius: 5px;
  margin: 1px;
}

.el-button-group > .el-button:last-child {
  border-radius: 5px;
  margin: 1px;
}

</style>
<script>
import Conference from './components/conference'
import { onMounted, reactive, computed, onBeforeUpdate,  watch} from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from "vuex"

export default {
  name: 'Home',

  components: {
    Conference
  },

  setup () {
    const store = useStore()
    const router = useRouter()
    const state = reactive({
      conference_category: '',
      page: 0,
      size: 20,
      title: computed(() => store.getters["root/getTitle"]),
      isSortTime: "basic",    //  기본 - 시간 정렬 없음
      isSortTitle: "basic",   //  기본 - 제목 정렬 없음
      sortType: '',   // 토글하기 위해 sortTime과 sortTitle을 분리하지 않음
      order: false,
      responseConference: [], // 컨퍼런스 목록 조회 응답
      errorMessage: "존재하는 컨퍼런스가 없습니다. 새롭게 컨퍼런스를 생성하시거나 다른 제목으로 컨퍼런스를 검색해주세요.",
      errTest: [],
      isLastPage: false,
    })
    watch(()=>state.title,(title) =>{
      console.log("watch",state.title);
      state.page = 0;
      getConferenceList()
    })

    const load = function () {
      if (!state.isLastPage) {
        console.log("로드 실행")
        state.page += 1
        addConferenceList()
        console.log('페이지 카운트 증가', state.page)
      }
    }

    const clickConference = function (id) {
      router.push({
        name: 'conference-detail',
        params: {
          conferenceId: state.responseConference[id - 1].id
        }
      })
    }

    // 방 목록 리스트 가져오기
    const getConferenceList = function () {
     // state.title = store.getters["root/getTitle"]  // 검색한 문자열 업데이트
      console.log("getConferenceList")
      console.log("title",state.title)
      console.log("titlecheck",state.title_check)
      store
        .dispatch('root/requestConferenceList', {
          conference_category: state.conference_category,
          page: state.page,
          size: state.size,
          title: state.title,
          sortType: state.sortType,
          order: state.order
        })
        .then(function(res) {
          state.responseConference = res.data.content
          console.log('컨퍼런스 목록 받아오기', state.responseConference)
        })
        .catch(function(err) {
          console.log(err)
        })
    }

    onMounted(() => {
      getConferenceList()
    })

    // Infinite Scroll
    const addConferenceList = function () {
      store
        .dispatch('root/requestConferenceList', {
          conference_category: state.conference_category,
          page: state.page,
          size: state.size,
          title: state.title,
          sortType: state.sortType,
          order: state.order
        })
        .then(function(res) {
          // 마지막 페이지 체크
          if (res.data.content.length < 20) {
            state.isLastPage = true
          }

          for (let val of res.data.content) {
            // Object.assign(state.responseConference, val)
            let number = state.responseConference.length
            state.responseConference[number] = val
          }
        })
        .catch(function(err) {
          console.log(err)
        })
    }

    // 처음 "시간 정렬"을 클릭 했을 경우
    const clickFirstTimeSort = function() {
      state.isSortTime = true
      state.isSortTitle = 'basic'
      state.sortType = 'timeSort'
      state.order = true
      store
        .dispatch('root/requestSortConferenceList', {
          conference_category: state.conference_category,
          page: 0,  // Infinite Scroll로 받아온 데이터까지 같이 정렬하는 건지? -> 현재는 Front 데이터 받아오는 문제가 있음
          // page: state.page,
          size: state.size,
          sortType: state.sortType,
          order: state.order
        })
        .then(function(res) {
          // console.log('처음 시간 정렬', res)
          state.responseConference = res.data.content
        })
        .catch(function(err) {
          console.log(err)
        })
    }

    // 처음 이후 "시간 정렬" 클릭 시
    const clickTimeSort = function() {
      state.isSortTime = !state.isSortTime
      state.isSortTitle = 'basic'
      state.sortType = 'timeSort'
      state.order = !state.order
      store
        .dispatch('root/requestSortConferenceList', {
          conference_category: state.conference_category,
          page: 0,
          // page: state.page,
          size: state.size,
          sortType: state.sortType,
          order: state.order
        })
        .then(function(res) {
          // console.log('처음 이후 시간 정렬', res.data.content)
          state.responseConference = res.data.content
        })
        .catch(function(err) {
          console.log(err)
        })
    }

    // 처음 "제목 정렬"을 클릭 했을 경우
    const clickFirstTitleSort = function() {
      state.isSortTitle = true
      state.isSortTime = 'basic'
      state.sortType = 'titleSort'
      state.order = true
      store
        .dispatch('root/requestSortConferenceList', {
          conference_category: state.conference_category,
          page: 0,
          // page: state.page,
          size: state.size,
          sortType: state.sortType,
          order: state.order
        })
        .then(function(res) {
          // console.log('처음 제목 정렬', res.data.content)
          state.responseConference = res.data.content
        })
        .catch(function(err) {
          console.log(err)
        })
    }

    // 처음 이후 "제목 정렬" 클릭 시
    const clickTitleSort = function() {
      state.isSortTitle = !state.isSortTitle
      state.isSortTime = 'basic'
      state.sortType = 'titleSort'
      state.order = !state.order
      store
        .dispatch('root/requestSortConferenceList', {
          conference_category: state.conference_category,
          page: 0,
          // page: state.page,
          size: state.size,
          sortType: state.sortType,
          order: state.order
        })
        .then(function(res) {
          // console.log('처음 이후 제목 정렬', res.data.content)
          state.responseConference = res.data.content
        })
        .catch(function(err) {
          console.log(err)
        })
    }

    return { state, load, clickConference, clickFirstTimeSort, clickTimeSort, clickFirstTitleSort, clickTitleSort, addConferenceList }
  }
}
</script>
