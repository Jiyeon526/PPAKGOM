<template>
  <h2>스터디 일정</h2>
  <div class="schedule-div">
    <el-button type="success" icon="el-icon-plus" circle @click="onClickCalendar()"></el-button>
    <DatePicker
      style="width:700px"
      v-model="state.date"
      timezone=""
      :attributes="state.attributes"
    />
  </div>
</template>

<script>
import { Calendar, DatePicker, VCalendar } from 'v-calendar';
import { onMounted, reactive } from 'vue'
import { useStore } from 'vuex'

export default {
  name: 'studyschedule',
  components: {
    Calendar,
    DatePicker,
  },
  setup (props, { emit }) {
    const store = useStore()
    const state = reactive({
      date: new Date(),
      attributes: [
        {
          bar: true,
          dates: [
            new Date(2021, 7, 1),
            new Date(2021, 7, 10),
            new Date(2021, 7, 22),
          ],
          popover: {  // Title or content
            label: "흐아어아"
          }
        },
        {
          bar: 'red',
          dates: [
            new Date(2021, 7, 4),
            new Date(2021, 7, 10),
            new Date(2021, 7, 15),
          ],
          popover: {
            label: "흐아어아\n으어어어흐아어아\n으어어어흐아어아\n으어어어"
          }
        },
        {
          bar: {
            style: {
              backgroundColor: 'brown',
            },
          },
          dates: [
            new Date(2021, 7, 12),
            new Date(2021, 7, 26),
            new Date(2021, 7, 15),
          ],
          popover: {
            label: "흐아어아"
          }
        },
      ]
    })

    const onClickCalendar = () => {
      emit("openStudyscheduleDialog")
    }

    // 페이지 진입시 불리는 훅
    onMounted (() => {

    })
    return { state, onClickCalendar }
  }
}
</script>
<style scoped>
.schedule-div {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.schedule-div > .el-button {
  margin: 5px;
}
</style>
