<template>
  <h2>스터디 출석현황</h2>
</template>

<script>
import { onMounted } from 'vue'
import { useStore } from 'vuex'

export default {
  name: 'studyattitude',
  setup () {
    const store = useStore()

    // 페이지 진입시 불리는 훅
    onMounted (() => {

    })
  }
}
</script>
