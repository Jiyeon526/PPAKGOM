<template>
  <h1 style="font-size:35px;">스터디 일정</h1>
  <div style="width: 80%; margin-left: auto; margin-right: auto;">
    <div style="float:right; margin:5px">
      <el-button type="success" circle class="el-icon-plus" @click="onClickCalendar"></el-button>
    </div>
    <Studyschedulecomponent :studyId="state.studyId" />
  </div>
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
      page: false,
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
