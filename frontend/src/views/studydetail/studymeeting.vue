<template>
  <h2>화상회의</h2>
</template>

<script>
import { onMounted } from 'vue'
import { useStore } from 'vuex'

export default {
  name: 'studymeeting',
  setup () {
    const store = useStore()

    // 페이지 진입시 불리는 훅
    onMounted (() => {

    })
  }
}
</script>
