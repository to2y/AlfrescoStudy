<template>
  <div>
    <h2>{{modelName}}</h2>
    <div class="action-header">
      <router-link to="/" tag="b-button">&lt;&lt;Back</router-link>
      <router-link :to="{path: '/type/create/' + modelName}" tag='b-button'>Create Type</router-link>
    </div>
    <h3>Types</h3>
    <b-table show-empty striped
           :items="typeList.entries" :fields="fields">
           <template slot="name" slot-scope="data">
             {{data.item.entry.prefixedName}}
           </template>
           <template slot="title" slot-scope="data">
             {{data.item.entry.title}}
           </template>
           <template slot="parent" slot-scope="data">
             {{data.item.entry.parentName}}
           </template>
    </b-table>

    <h3>Aspects</h3>
    <b-table show-empty striped
           :items="aspectList.entries" :fields="fields">
           <template slot="name" slot-scope="data">
             {{data.item.entry.prefixedName}}
           </template>
           <template slot="title" slot-scope="data">
             {{data.item.entry.title}}
           </template>
           <template slot="parent" slot-scope="data">
             {{data.item.entry.parentName}}
           </template>
    </b-table>
  </div>
</template>

<script>
import Alf from '@/alfresco/alfresco.js'

export default {
  name: 'Types',
  props: ['modelName'],
  data () {
    return {
      typeList: [],
      fields: ['name', 'title', 'parent'],
      aspectList: [],
      aspectFields: ['name', 'title', 'parent']
    }
  },
  created () {
    this.getTypes()
    this.getAspects()
  },
  methods: {
    getTypes () {
      Alf.core.customModelApi.getAllCustomType(this.modelName).then(data => {
        console.log('API called successfully. Returned data: ' + data)
        this.typeList = data.list
        console.log(this.typeList)
      }, error => {
        console.error(error)
      })
    },
    getAspects () {
      Alf.core.customModelApi.getAllCustomAspect(this.modelName).then(data => {
        console.log('API called successfully. Returned data: ' + data)
        this.aspectList = data.list
        console.log(this.typeList)
      }, error => {
        console.error(error)
      })
    }
  }
}
</script>

<style>
.action-header {
  text-align:left;
  margin-bottom: 4px;
}
</style>
