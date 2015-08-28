package me.xiaok.waveplayer.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by GeeKaven on 15/8/28.
 */
public class FmArtist {

    /**
     * artist : {"name":"周杰倫","mbid":"a223958d-5c56-4b2c-a30a-87e357bc121b","url":"http://www.last.fm/music/+noredirect/%E5%91%A8%E6%9D%B0%E5%80%AB","image":[{"#text":"http://img2-ak.lst.fm/i/u/34s/e2e71119209a48f4877393afcc8cf651.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/e2e71119209a48f4877393afcc8cf651.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/e2e71119209a48f4877393afcc8cf651.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/e2e71119209a48f4877393afcc8cf651.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/e2e71119209a48f4877393afcc8cf651.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/e2e71119209a48f4877393afcc8cf651.png","size":""}],"streamable":"0","ontour":"0","stats":{"listeners":"118701","playcount":"9548521"},"similar":{"artist":[{"name":"林俊傑","url":"http://www.last.fm/music/%E6%9E%97%E4%BF%8A%E5%82%91","image":[{"#text":"http://img2-ak.lst.fm/i/u/34s/4a02164ad93d40a0c0db06b873b245c0.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/4a02164ad93d40a0c0db06b873b245c0.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/4a02164ad93d40a0c0db06b873b245c0.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/4a02164ad93d40a0c0db06b873b245c0.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/4a02164ad93d40a0c0db06b873b245c0.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/4a02164ad93d40a0c0db06b873b245c0.png","size":""}]},{"name":"王力宏","url":"http://www.last.fm/music/%E7%8E%8B%E5%8A%9B%E5%AE%8F","image":[{"#text":"http://img2-ak.lst.fm/i/u/34s/f2240ede1cc64eaa9b963770f059e1b4.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/f2240ede1cc64eaa9b963770f059e1b4.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/f2240ede1cc64eaa9b963770f059e1b4.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/f2240ede1cc64eaa9b963770f059e1b4.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/f2240ede1cc64eaa9b963770f059e1b4.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/f2240ede1cc64eaa9b963770f059e1b4.png","size":""}]},{"name":"五月天","url":"http://www.last.fm/music/%E4%BA%94%E6%9C%88%E5%A4%A9","image":[{"#text":"http://img2-ak.lst.fm/i/u/34s/9b8a9b5f240e415da9cce6a6a534f7b7.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/9b8a9b5f240e415da9cce6a6a534f7b7.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/9b8a9b5f240e415da9cce6a6a534f7b7.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/9b8a9b5f240e415da9cce6a6a534f7b7.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/9b8a9b5f240e415da9cce6a6a534f7b7.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/9b8a9b5f240e415da9cce6a6a534f7b7.png","size":""}]},{"name":"陶喆","url":"http://www.last.fm/music/%E9%99%B6%E5%96%86","image":[{"#text":"http://img2-ak.lst.fm/i/u/34s/3408c42d9e0045738fdfe92c24ca84ff.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/3408c42d9e0045738fdfe92c24ca84ff.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/3408c42d9e0045738fdfe92c24ca84ff.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/3408c42d9e0045738fdfe92c24ca84ff.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/3408c42d9e0045738fdfe92c24ca84ff.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/3408c42d9e0045738fdfe92c24ca84ff.png","size":""}]},{"name":"梁靜茹","url":"http://www.last.fm/music/%E6%A2%81%E9%9D%9C%E8%8C%B9","image":[{"#text":"http://img2-ak.lst.fm/i/u/34s/cd6e0d47c23b48fe8866ec970e4a2051.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/cd6e0d47c23b48fe8866ec970e4a2051.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/cd6e0d47c23b48fe8866ec970e4a2051.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/cd6e0d47c23b48fe8866ec970e4a2051.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/cd6e0d47c23b48fe8866ec970e4a2051.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/cd6e0d47c23b48fe8866ec970e4a2051.png","size":""}]}]},"tags":{"tag":[{"name":"chinese","url":"http://www.last.fm/tag/chinese"},{"name":"C-pop","url":"http://www.last.fm/tag/C-pop"},{"name":"Jay Chou","url":"http://www.last.fm/tag/Jay+Chou"},{"name":"pop","url":"http://www.last.fm/tag/pop"},{"name":"cpop","url":"http://www.last.fm/tag/cpop"}]},"bio":{"links":{"link":{"#text":"","rel":"original","href":"http://last.fm/music/%E5%91%A8%E6%9D%B0%E5%80%AB/+wiki"}},"published":"22 Dec 2006, 13:08","summary":"周杰伦（1979年1月18日－）是中国台湾歌手、演员、导演。\n\n周杰伦生于台北县林口乡（今新北市林口区），父亲周耀中是一位生物教师，母亲叶惠美则是美术教师。自小周杰伦就对音乐表现出浓厚的兴趣与极高的天赋，而且喜欢模仿歌星、演员表演和变魔术。他3岁开始学习钢琴，16岁尝试作曲。高中就读于淡江中学音乐科，主修钢琴，副修大提琴，为将来的音乐发展打下了深厚的基础。 <a href=\"http://www.last.fm/music/%E5%91%A8%E6%9D%B0%E5%80%AB\">Read more on Last.fm<\/a>.","content":"周杰伦（1979年1月18日－）是中国台湾歌手、演员、导演。\n\n周杰伦生于台北县林口乡（今新北市林口区），父亲周耀中是一位生物教师，母亲叶惠美则是美术教师。自小周杰伦就对音乐表现出浓厚的兴趣与极高的天赋，而且喜欢模仿歌星、演员表演和变魔术。他3岁开始学习钢琴，16岁尝试作曲。高中就读于淡江中学音乐科，主修钢琴，副修大提琴，为将来的音乐发展打下了深厚的基础。\n\n1997年周杰伦借与同学参加选秀的机会展示了杰出的作曲技巧，被吴宗宪赞赏并获得签约，开始为其他歌手作曲、填词。2000年周杰伦走向幕前发布了处女作个人同名专辑。此后他除2009年外几乎保持了一年一张专辑的创作速度，并迅速成为华语乐坛的领军人物，在亚太地区获得极大关注和成功。\n\n虽然周杰伦的音乐被笼统归类为流行乐，但其作品中经常包含节奏布鲁斯、嘻哈音乐、摇滚等风格，并形成所谓周氏风格。他将传统的中国音乐风格和乐器演奏与节奏布鲁斯、摇滚融合起来，形成中国风。有些作品甚至是以五音音阶来写作的，更突出了传统的东方风格。除了善于应用自己民族的音乐风格外，周的音乐还广泛借鉴其他国家的音乐风格。\n\n周杰伦的音乐通常由方文山、黃俊郎等几位填词，词风和周的个人风格气质相符恰到好处。其中方文山的词尤为含义丰富、想象瑰丽、情感充沛。其中的中国风作品往往以古词的格式写成，富有中国意味。周的作品主题涉猎广泛，大大突破了以往华语流行乐的桎梏，包含爱情、战争、武术、侦探故事、家庭暴力等等。\n\n从2005年的《头文字D》开始，周杰伦的事业拓展到电影领域。并接连主演了《满城尽带黄金甲》、《不能说的秘密》、《刺陵》、《青蜂侠》等电影。其中《不能说的秘密》由周杰伦亲自执导。 <a href=\"http://www.last.fm/music/%E5%91%A8%E6%9D%B0%E5%80%AB\">Read more on Last.fm<\/a>. User-contributed text is available under the Creative Commons By-SA License; additional terms may apply."}}
     */

    private ArtistEntity artist;

    public void setArtist(ArtistEntity artist) {
        this.artist = artist;
    }

    public ArtistEntity getArtist() {
        return artist;
    }

    public static class ArtistEntity {
        /**
         * name : 周杰倫
         * mbid : a223958d-5c56-4b2c-a30a-87e357bc121b
         * url : http://www.last.fm/music/+noredirect/%E5%91%A8%E6%9D%B0%E5%80%AB
         * image : [{"#text":"http://img2-ak.lst.fm/i/u/34s/e2e71119209a48f4877393afcc8cf651.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/e2e71119209a48f4877393afcc8cf651.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/e2e71119209a48f4877393afcc8cf651.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/e2e71119209a48f4877393afcc8cf651.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/e2e71119209a48f4877393afcc8cf651.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/e2e71119209a48f4877393afcc8cf651.png","size":""}]
         * streamable : 0
         * ontour : 0
         * stats : {"listeners":"118701","playcount":"9548521"}
         * similar : {"artist":[{"name":"林俊傑","url":"http://www.last.fm/music/%E6%9E%97%E4%BF%8A%E5%82%91","image":[{"#text":"http://img2-ak.lst.fm/i/u/34s/4a02164ad93d40a0c0db06b873b245c0.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/4a02164ad93d40a0c0db06b873b245c0.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/4a02164ad93d40a0c0db06b873b245c0.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/4a02164ad93d40a0c0db06b873b245c0.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/4a02164ad93d40a0c0db06b873b245c0.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/4a02164ad93d40a0c0db06b873b245c0.png","size":""}]},{"name":"王力宏","url":"http://www.last.fm/music/%E7%8E%8B%E5%8A%9B%E5%AE%8F","image":[{"#text":"http://img2-ak.lst.fm/i/u/34s/f2240ede1cc64eaa9b963770f059e1b4.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/f2240ede1cc64eaa9b963770f059e1b4.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/f2240ede1cc64eaa9b963770f059e1b4.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/f2240ede1cc64eaa9b963770f059e1b4.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/f2240ede1cc64eaa9b963770f059e1b4.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/f2240ede1cc64eaa9b963770f059e1b4.png","size":""}]},{"name":"五月天","url":"http://www.last.fm/music/%E4%BA%94%E6%9C%88%E5%A4%A9","image":[{"#text":"http://img2-ak.lst.fm/i/u/34s/9b8a9b5f240e415da9cce6a6a534f7b7.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/9b8a9b5f240e415da9cce6a6a534f7b7.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/9b8a9b5f240e415da9cce6a6a534f7b7.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/9b8a9b5f240e415da9cce6a6a534f7b7.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/9b8a9b5f240e415da9cce6a6a534f7b7.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/9b8a9b5f240e415da9cce6a6a534f7b7.png","size":""}]},{"name":"陶喆","url":"http://www.last.fm/music/%E9%99%B6%E5%96%86","image":[{"#text":"http://img2-ak.lst.fm/i/u/34s/3408c42d9e0045738fdfe92c24ca84ff.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/3408c42d9e0045738fdfe92c24ca84ff.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/3408c42d9e0045738fdfe92c24ca84ff.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/3408c42d9e0045738fdfe92c24ca84ff.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/3408c42d9e0045738fdfe92c24ca84ff.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/3408c42d9e0045738fdfe92c24ca84ff.png","size":""}]},{"name":"梁靜茹","url":"http://www.last.fm/music/%E6%A2%81%E9%9D%9C%E8%8C%B9","image":[{"#text":"http://img2-ak.lst.fm/i/u/34s/cd6e0d47c23b48fe8866ec970e4a2051.png","size":"small"},{"#text":"http://img2-ak.lst.fm/i/u/64s/cd6e0d47c23b48fe8866ec970e4a2051.png","size":"medium"},{"#text":"http://img2-ak.lst.fm/i/u/174s/cd6e0d47c23b48fe8866ec970e4a2051.png","size":"large"},{"#text":"http://img2-ak.lst.fm/i/u/300x300/cd6e0d47c23b48fe8866ec970e4a2051.png","size":"extralarge"},{"#text":"http://img2-ak.lst.fm/i/u/cd6e0d47c23b48fe8866ec970e4a2051.png","size":"mega"},{"#text":"http://img2-ak.lst.fm/i/u/arQ/cd6e0d47c23b48fe8866ec970e4a2051.png","size":""}]}]}
         * tags : {"tag":[{"name":"chinese","url":"http://www.last.fm/tag/chinese"},{"name":"C-pop","url":"http://www.last.fm/tag/C-pop"},{"name":"Jay Chou","url":"http://www.last.fm/tag/Jay+Chou"},{"name":"pop","url":"http://www.last.fm/tag/pop"},{"name":"cpop","url":"http://www.last.fm/tag/cpop"}]}
         * bio : {"links":{"link":{"#text":"","rel":"original","href":"http://last.fm/music/%E5%91%A8%E6%9D%B0%E5%80%AB/+wiki"}},"published":"22 Dec 2006, 13:08","summary":"周杰伦（1979年1月18日－）是中国台湾歌手、演员、导演。\n\n周杰伦生于台北县林口乡（今新北市林口区），父亲周耀中是一位生物教师，母亲叶惠美则是美术教师。自小周杰伦就对音乐表现出浓厚的兴趣与极高的天赋，而且喜欢模仿歌星、演员表演和变魔术。他3岁开始学习钢琴，16岁尝试作曲。高中就读于淡江中学音乐科，主修钢琴，副修大提琴，为将来的音乐发展打下了深厚的基础。 <a href=\"http://www.last.fm/music/%E5%91%A8%E6%9D%B0%E5%80%AB\">Read more on Last.fm<\/a>.","content":"周杰伦（1979年1月18日－）是中国台湾歌手、演员、导演。\n\n周杰伦生于台北县林口乡（今新北市林口区），父亲周耀中是一位生物教师，母亲叶惠美则是美术教师。自小周杰伦就对音乐表现出浓厚的兴趣与极高的天赋，而且喜欢模仿歌星、演员表演和变魔术。他3岁开始学习钢琴，16岁尝试作曲。高中就读于淡江中学音乐科，主修钢琴，副修大提琴，为将来的音乐发展打下了深厚的基础。\n\n1997年周杰伦借与同学参加选秀的机会展示了杰出的作曲技巧，被吴宗宪赞赏并获得签约，开始为其他歌手作曲、填词。2000年周杰伦走向幕前发布了处女作个人同名专辑。此后他除2009年外几乎保持了一年一张专辑的创作速度，并迅速成为华语乐坛的领军人物，在亚太地区获得极大关注和成功。\n\n虽然周杰伦的音乐被笼统归类为流行乐，但其作品中经常包含节奏布鲁斯、嘻哈音乐、摇滚等风格，并形成所谓周氏风格。他将传统的中国音乐风格和乐器演奏与节奏布鲁斯、摇滚融合起来，形成中国风。有些作品甚至是以五音音阶来写作的，更突出了传统的东方风格。除了善于应用自己民族的音乐风格外，周的音乐还广泛借鉴其他国家的音乐风格。\n\n周杰伦的音乐通常由方文山、黃俊郎等几位填词，词风和周的个人风格气质相符恰到好处。其中方文山的词尤为含义丰富、想象瑰丽、情感充沛。其中的中国风作品往往以古词的格式写成，富有中国意味。周的作品主题涉猎广泛，大大突破了以往华语流行乐的桎梏，包含爱情、战争、武术、侦探故事、家庭暴力等等。\n\n从2005年的《头文字D》开始，周杰伦的事业拓展到电影领域。并接连主演了《满城尽带黄金甲》、《不能说的秘密》、《刺陵》、《青蜂侠》等电影。其中《不能说的秘密》由周杰伦亲自执导。 <a href=\"http://www.last.fm/music/%E5%91%A8%E6%9D%B0%E5%80%AB\">Read more on Last.fm<\/a>. User-contributed text is available under the Creative Commons By-SA License; additional terms may apply."}
         */

        private String name;
        private String url;
        private BioEntity bio;
        private List<ImageEntity> image;

        public void setName(String name) {
            this.name = name;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setBio(BioEntity bio) {
            this.bio = bio;
        }

        public void setImage(List<ImageEntity> image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public BioEntity getBio() {
            return bio;
        }

        public List<ImageEntity> getImage() {
            return image;
        }

        public static class BioEntity {
            private String summary;
            private String content;

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getSummary() {
                return summary;
            }

            public String getContent() {
                return content;
            }
        }

        public static class ImageEntity {
            /**
             * #text : http://img2-ak.lst.fm/i/u/34s/e2e71119209a48f4877393afcc8cf651.png
             * size : small
             */

            @SerializedName("#text")
            private String imageUrl;
            private String size;

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public String getSize() {
                return size;
            }
        }
    }
}


