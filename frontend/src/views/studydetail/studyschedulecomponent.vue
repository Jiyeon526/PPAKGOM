<template>
  <v-calendar
    class="custom-calendar max-w-full"
    :masks="state.masks"
    :attributes="state.attributes"
    disable-page-swipe
    is-expanded
  >
    <template v-slot:day-content="{ day, attributes }">
      <div class="flex flex-col h-full z-10 overflow-hidden">
        <span class="day-label text-sm text-gray-900">{{ day.day }}</span>
        <div class="flex-grow overflow-y-auto overflow-x-auto">
          <p
            v-for="attr in attributes"
            :key="attr.id"
            class="text-xs leading-tight rounded-sm p-1 mt-0 mb-1 text-color"
            :class="attr.customData.color"
          >
            {{ attr.customData.title }}
          </p>
        </div>
      </div>
    </template>
  </v-calendar>
</template>
<script>

// import { Calendar, DatePicker, VCalendar } from 'v-calendar';
import { computed, onMounted, reactive, watch, ref } from 'vue'
import { useStore } from 'vuex'
import { ElMessage } from "element-plus";

export default {
  name: 'studyschedule',
  components: {

  },
  props: {
    studyId: {
      type: Number,
    }
  },

  setup (props, { emit }) {
    const store = useStore()
    const month = new Date().getMonth();
    const year = new Date().getFullYear();
    const state = reactive({
      studyId: computed(() => props.studyId),
      reload: computed(() => store.getters["root/getReload"]),
      month: month + 1,
      masks: {
        weekdays: 'WWW',
      },
      attributes: [],
    })

    watch(
      () => state.reload,
      () => {
        getScheduleList()
      }
    )

    watch(
      () => state.studyId,
      () => {
        getScheduleList()
      }
    )

    const getScheduleList = () => {
      store
        .dispatch('root/requestScheduleInfo', {
          month: state.month,
          studyId: state.studyId
        })
          .then(function(res) {
            state.attributes = res.data
          })
          .catch(function(err) {
            ElMessage({
              type: "error",
              message: err.message
            })
          })
    }

    // 페이지 진입시 불리는 훅
    onMounted (() => {
      getScheduleList()
    })

    return { state, month, year, getScheduleList }
  }
};
</script>

<style>
/* 글자 배경색 class */
.red {
  background-color: #e53e3e;
}
.orange {
  background-color: #ed8936;
}
.green {
  background-color: #48bb78;
}
.indigo {
  background-color: #667eea;
}
.purple {
  background-color: #9f7aea;
}

.text-color {
  color: white;
  padding: 0px 2px;
  margin: 6px 0px;
}

::-webkit-scrollbar {
  width: 0px;
}
::-webkit-scrollbar-track {
  display: none;
}

.custom-calendar.vc-container {
  --day-border: 1px solid #b8c2cc;
  --day-border-highlight: 1px solid #b8c2cc;
  --day-width: 80px;
  --day-height: 80px;
  --weekday-bg: #f8fafc;
  --weekday-border: 1px solid #eaeaea;
  border-radius: 0;
  width: 100%;
}
.custom-calendar.vc-container .vc-header {
  background-color: #f1f5f8;
  padding: 10px 0;
}
.custom-calendar.vc-container .vc-weeks {
  padding: 0;
}
.custom-calendar.vc-container .vc-weekday {
  background-color: var(--weekday-bg);
  border-bottom: var(--weekday-border);
  border-top: var(--weekday-border);
  padding: 5px 0;
}
.custom-calendar.vc-container .vc-day {
  overflow: scroll;
  padding: 0 5px 0px 5px;
  text-align: left;
  height: var(--day-height);
  min-width: var(--day-width);
  background-color: white;
}

.custom-calendar.vc-container .vc-day .weekday-1, .vc-day .weekday-7 {
  background-color: #eff8ff;
}
.custom-calendar.vc-container .vc-day:not(.on-bottom) {
  border-bottom: var(--day-border);
}
.custom-calendar.vc-container .vc-day:not(.on-bottom).weekday-1 {
  border-bottom: var(--day-border-highlight);
}
.custom-calendar.vc-container .vc-day:not(.on-right) {
  border-right: var(--day-border);
}
.custom-calendar.vc-container .vc-day-dots {
  margin-bottom: 5px;
}
</style>
