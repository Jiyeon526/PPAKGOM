<template>
  <h2>빡곰 스터디</h2>
  <div class="study-home-div">
    <div @click="onClickAttitude">차트</div>
    <div @click="onClicSchedule">달력</div>
  </div>
  <div class="study-home-div">
    <div @click="onClickWorkbook">문제집</div>
    <div @click="onClickScore">점수</div>
  </div>
</template>

<script>
import { onMounted, reactive } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from "vue-router"

export default {
  name: 'studyhome',
  setup () {
    const store = useStore()
    const router = useRouter()
    const state = reactive({

    })
    const onClickAttitude = () => {
      store.commit("root/setSelectOption", "studyattitude");
      router.push({
        name: "studyattitude"
      })
    }
    const onClicSchedule = () => {
      store.commit("root/setSelectOption", "studyschedule");
      router.push({
        name: "studyschedule"
      })
    }
    const onClickWorkbook = () => {
      store.commit("root/setSelectOption", "studyworkbook");
      router.push({
        name: "studyworkbook"
      })
    }
    const onClickScore = () => {
      store.commit("root/setSelectOption", "studyscore");
      router.push({
        name: "studyscore"
      })
    }
    // 페이지 진입시 불리는 훅
    onMounted (() => {

    })
    return { state, onClickScore, onClickAttitude, onClicSchedule, onClickWorkbook }
  }
}
</script>
<style scoped>
.study-home-div {
  height: 300px;
  width: 100%;
  display: flex;
  justify-content: space-evenly;
  align-items: center;
}
</style>
