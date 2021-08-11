<template>
  <h2>스터디 일정</h2>
  <Calendar
    style="width:700px"
    :attributes="state.attrs"
    v-model="state.date"
  />
  <!-- <DatePicker v-model="date" /> -->
</template>

<script>
import { Calendar, DatePicker } from 'v-calendar';
import { onMounted, reactive } from 'vue'
import { useStore } from 'vuex'

export default {
  name: 'studyschedule',
  components: {
    Calendar,
    DatePicker,
  },
  setup () {
    const store = useStore()
    const state = reactive({
      attrs: [
        {
          key: 'today',
          highlight: true,
          dates: new Date(),
        },
      ],
      date: new Date(),
    })
    // 페이지 진입시 불리는 훅
    onMounted (() => {

    })
    return { state }
  }
}
</script>
