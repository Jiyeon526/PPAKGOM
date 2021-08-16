<template>
  <h2>스터디 일정</h2>
  <div style="float:right; margin:5px">
    <el-button type="success" circle class="el-icon-plus" @click="onClickCalendar"></el-button>
  </div>
  <Studyschedulecomponent :studyId="state.studyId" />
</template>
<script>

import Studyschedulecomponent from "@/views/studydetail/studyschedulecomponent"
import { computed, onMounted, reactive, watch } from 'vue'
import { useStore } from 'vuex'

export default {
  name: 'studyschedule',
  components: {
    Studyschedulecomponent,
  },

  setup (props, { emit }) {
    const store = useStore()
    const month = new Date().getMonth();
    const year = new Date().getFullYear();
    const state = reactive({
      studyId: computed(() => store.getters["root/getStudypk"]),
    })

    const onClickCalendar = () => {
      emit("openStudyscheduleDialog", state.studyPk)
    }

    onMounted (() => {

    })

    return { state, month, year, onClickCalendar }
  }
};
</script>

<style>

</style>
