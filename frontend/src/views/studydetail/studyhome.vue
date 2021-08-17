<template>
  <h1 style="font-size:35px;">스터디 홈</h1>
  <div class="study-home-div">
    <div @click="onClickAttitude" style="width: 50%; margin: 10px;">
      <h4>차트</h4>
    </div>
    <div @click="onClicSchedule" style="width: 50%; margin: 10px;">
      <h4>스터디 일정</h4>
      <Studyschedulecomponent :studyId="state.studyId" />
    </div>
  </div>
  <div class="study-home-div">
    <div @click="onClickWorkbook" style="width: 50%; margin: 10px;">
      <h4>문제집</h4>
    </div>
    <div @click="onClickScore" style="width: 50%; margin: 10px;">
      <h4>점수</h4>
    </div>
  </div>
</template>

<script>
import Studyschedulecomponent from "@/views/studydetail/studyschedulecomponent"
import { onMounted, reactive, computed } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from "vue-router"

export default {
  name: 'studyhome',
  components: {
    Studyschedulecomponent,
  },
  setup () {
    const store = useStore()
    const router = useRouter()
    const state = reactive({
      studyId: computed(() => store.getters["root/getStudypk"]),
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
        name: "studyschedule",
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
  width: 100%;
  height: auto;
  display: flex;
  justify-content: space-evenly;
  align-items: center;
}
</style>
