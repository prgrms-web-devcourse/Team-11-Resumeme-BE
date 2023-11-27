package org.devcourse.resumeme.business.resume.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.devcourse.resumeme.business.resume.entity.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.lang.Double.parseDouble;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.devcourse.resumeme.business.resume.domain.Property.DEGREE;
import static org.devcourse.resumeme.business.resume.domain.Property.DESCRIPTION;
import static org.devcourse.resumeme.business.resume.domain.Property.MAJOR;
import static org.devcourse.resumeme.business.resume.domain.Property.MAX_SCORE;
import static org.devcourse.resumeme.business.resume.domain.Property.SCORE;
import static org.devcourse.resumeme.business.resume.domain.Property.TRAINING;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Training implements Converter {

    private String explanation;

    private EducationalDetails educationalDetails;

    private DateDetails dateDetails;

    private GPADetails gpaDetails;

    @Builder
    public Training(String organization, String major, String degree, LocalDate admissionDate,
            LocalDate graduationDate, double gpa, double maxGpa, String explanation) {
        this.educationalDetails = new EducationalDetails(organization, major, degree);
        this.dateDetails = new DateDetails(admissionDate, graduationDate);
        this.gpaDetails = new GPADetails(gpa, maxGpa);
        this.explanation = explanation;
    }

    @Override
    public Component toComponent(Long resumeId) {
        Component explanation = new Component(DESCRIPTION, this.explanation, resumeId);
        Component degree = new Component(DEGREE, educationalDetails.getDegree(), resumeId);
        Component major = new Component(MAJOR, educationalDetails.getMajor(), resumeId);
        Component gpa = new Component(SCORE, String.valueOf(gpaDetails.getGpa()), resumeId);
        Component maxGpa = new Component(MAX_SCORE, String.valueOf(gpaDetails.getMaxGpa()), resumeId);

        return new Component(TRAINING, this.educationalDetails.getOrganization(), this.dateDetails.getAdmissionDate(), this.dateDetails.getGraduationDate(),
                resumeId, List.of(explanation, degree, major, gpa, maxGpa));
    }

    private static Training of(List<Component> components) {
        TrainingConverter converter = TrainingConverter.of(components);

        return Training.of(converter);
    }

    private static Training of(TrainingConverter converter) {
        return Training.builder()
                .organization(converter.training.getContent())
                .admissionDate(converter.training.getStartDate())
                .graduationDate(converter.training.getEndDate())
                .major(converter.details.major.getContent())
                .degree(converter.details.degree.getContent())
                .gpa(parseDouble(converter.details.gpa.getContent()))
                .maxGpa(parseDouble(converter.details.maxGpa.getContent()))
                .explanation(converter.details.explanation.getContent())
                .build();
    }

    @Builder
    private static class TrainingConverter {

        private Component training;

        private TrainingDetails details;

        @Builder
        private static class TrainingDetails {

            private Component explanation;

            private Component degree;

            private Component major;

            private Component gpa;

            private Component maxGpa;

        }

        private static TrainingConverter of(List<Component> components) {
            Map<Property, Component> componentMap = components.stream()
                    .collect(toMap(Component::getProperty, identity()));

            TrainingDetails details = TrainingDetails.builder()
                    .explanation(componentMap.get(DESCRIPTION))
                    .degree(componentMap.get(DEGREE))
                    .major(componentMap.get(MAJOR))
                    .gpa(componentMap.get(SCORE))
                    .maxGpa(componentMap.get(MAX_SCORE))
                    .build();

            return TrainingConverter.builder()
                    .training(componentMap.get(TRAINING))
                    .details(details)
                    .build();
        }

    }

}
