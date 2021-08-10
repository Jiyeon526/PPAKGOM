<template>
  <h2>스터디 문제집</h2>
</template>

<script>
import { onMounted } from 'vue'
import { useStore } from 'vuex'

export default {
  name: 'studyworkbook',
  setup () {
    const store = useStore()

    // 페이지 진입시 불리는 훅
    onMounted (() => {

    })
  }
}
</script>
