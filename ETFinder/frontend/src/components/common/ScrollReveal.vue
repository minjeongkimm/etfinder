<template>
  <div ref="target" :class="['transition-opacity duration-1000 ease-out transform-gpu will-change-[opacity,transform]', visible ? 'opacity-100 translate-y-0 translate-z-0' : 'opacity-0 translate-y-10 translate-z-0']" style="backface-visibility: hidden;">
    <slot></slot>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';

const props = defineProps({
  threshold: {
    type: Number,
    default: 0.1
  },
  rootMargin: {
    type: String,
    default: '0px'
  }
});

const target = ref(null);
const visible = ref(false);
let observer = null;

onMounted(() => {
  observer = new IntersectionObserver(
    ([entry]) => {
      if (entry.isIntersecting) {
        visible.value = true;
        // 한 번 보이면 관찰 중단 (One-time animation)
        if (target.value) observer.unobserve(target.value);
      }
    },
    {
      threshold: props.threshold,
      rootMargin: props.rootMargin
    }
  );

  if (target.value) {
    observer.observe(target.value);
  }
});

onUnmounted(() => {
  if (observer) {
    observer.disconnect();
  }
});
</script>
